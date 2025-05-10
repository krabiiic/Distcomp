package controllers

import (
	"bytes"
	"context"
	"crypto/rand"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"log"
	"sync"
	"time"
	"errors"
	"github.com/labstack/echo/v4"
	"github.com/segmentio/kafka-go"
	"github.com/go-redis/redis/v8"

	"distributedcomputing/model"
)

const (
	kafkaBroker     = "localhost:9094"
	inTopic         = "InTopic"
	outTopic        = "OutTopic"
	kafkaTimeoutSec = 5
)


type NoteMessage struct {
	Action string `json:"action"`
	ID     string `json:"id,omitempty"`
	Data   string `json:"data,omitempty"`
}

type KafkaProducer struct {
	writer *kafka.Writer
}

func NewKafkaProducer() *KafkaProducer {
	writer := &kafka.Writer{
		Addr:     kafka.TCP(kafkaBroker),
		Topic:    inTopic,
		Balancer: &kafka.LeastBytes{},
		BatchSize:    1,                   
    	BatchTimeout: 1 * time.Millisecond,
		Async: false,
	}
	return &KafkaProducer{writer: writer}
}

func (kp *KafkaProducer) SendNoteMessage(ctx context.Context, action, id string, data any) error {
	var dataStr string
	if data != nil {
		b, err := json.Marshal(data)
		if err != nil {
			return err
		}
		dataStr = string(b)
	}

	msg := NoteMessage{Action: action, ID: id, Data: dataStr}
	b, err := json.Marshal(msg)
	if err != nil {
		return err
	}

	kafkaMsg := kafka.Message{
		Key:   []byte(id),
		Value: b,
	}
	return kp.writer.WriteMessages(ctx, kafkaMsg)
}

func generateRandomUint64() (uint64, error) {
	var b [4]byte
	_, err := rand.Read(b[:])
	if err != nil {
		return 0, err
	}
	return uint64(binary.LittleEndian.Uint32(b[:])), nil
}

var responseChannels sync.Map // map[string]chan string

func startKafkaResponseConsumer(ctx context.Context) {
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:        []string{kafkaBroker},
		Topic:          outTopic,
		GroupID:        "note-client-group",  
		MinBytes:       1,                   
		MaxBytes:       10e6,                 
		MaxWait:        10 * time.Millisecond,
		ReadLagInterval: -1,                  
	})
	defer reader.Close()

	for {
		m, err := reader.ReadMessage(ctx)
		if err != nil {
			log.Printf("Error reading message: %v", err)
			continue
		}

		var response NoteMessage
		decoder := json.NewDecoder(bytes.NewReader(m.Value))
		if err := decoder.Decode(&response); err != nil {
			log.Printf("Failed to unmarshal response: %v", err)
			continue
		}

		if chVal, ok := responseChannels.Load(response.ID); ok {
			if ch, ok := chVal.(chan string); ok {
				ch <- response.Data
				close(ch)
				responseChannels.Delete(response.ID)
			}
		}
	}
}

func awaitKafkaResponse(id string) (string, error) {
	ch := make(chan string, 1)
	responseChannels.Store(id, ch)
	defer responseChannels.Delete(id)

	select {
	case res := <-ch:
		return res, nil
	case <-time.After(kafkaTimeoutSec * time.Second):
		return "", fmt.Errorf("timeout waiting for response")
	}
}

func NewNoteController(e *echo.Echo, redisClient *redis.Client) {
	ctx := context.Background()
	go startKafkaResponseConsumer(ctx)
	kafkaProducer := NewKafkaProducer()

	e.GET("/api/v1.0/notes", func(c echo.Context) error {
		reqID := fmt.Sprint(time.Now().UnixNano())

		cachedNotes, err := redisClient.Get(ctx, reqID).Result()
		if err == nil {

			var notes []model.NoteResponseTo
			if err := json.Unmarshal([]byte(cachedNotes), &notes); err == nil {
				return c.JSON(200, notes)
			}
		}
		if err := kafkaProducer.SendNoteMessage(ctx, "get_all", reqID, nil); err != nil {
			return c.JSON(500, map[string]string{"error": err.Error()})
		}

		res, err := awaitKafkaResponse(reqID)
		if err != nil {
			return c.JSON(504, map[string]string{"error": err.Error()})
		}

		redisClient.Set(ctx, reqID, res, 0)
		return c.JSON(200, json.RawMessage(res))
	})

	e.POST("/api/v1.0/notes", func(c echo.Context) error {
		randomID, _ := generateRandomUint64()
		var req model.NoteRequestTo
		if err := c.Bind(&req); err != nil {
			return c.JSON(400, map[string]string{"error": "Invalid request"})
		}
		req.Id = int64(randomID)

		if err := kafkaProducer.SendNoteMessage(ctx, "create", fmt.Sprint(randomID), req); err != nil {
			return c.JSON(500, map[string]string{"error": err.Error()})
		}

		reqData, _ := json.Marshal(req)
		redisClient.Set(ctx, fmt.Sprint(req.Id), string(reqData), 0)

		return c.JSON(201, req)
	})

	e.PUT("/api/v1.0/notes", func(c echo.Context) error {
		var req model.NoteRequestTo
		if err := c.Bind(&req); err != nil {
			return c.JSON(400, map[string]string{"error": "Invalid request"})
		}
		id := fmt.Sprint(req.Id)

		if err := kafkaProducer.SendNoteMessage(ctx, "update", id, req); err != nil {
			return c.JSON(500, map[string]string{"error": err.Error()})
		}

		reqData, _ := json.Marshal(req)
		redisClient.Set(ctx, id, string(reqData), 0)

		res, err := awaitKafkaResponse(id)
		if err != nil {
			return c.JSON(504, map[string]string{"error": err.Error()})
		}
		return c.JSON(200, json.RawMessage(res))
	})

	e.DELETE("/api/v1.0/notes/:id", func(c echo.Context) error {
		id := c.Param("id")
		if err := kafkaProducer.SendNoteMessage(ctx, "delete", id, nil); err != nil {
			return c.JSON(500, map[string]string{"error": err.Error()})
		}

		redisClient.Del(ctx, id)

		res, err := awaitKafkaResponse(id)
		if err != nil {
			return c.JSON(504, map[string]string{"error": err.Error()})
		}
		return c.JSON(204, json.RawMessage(res))
	})

	e.GET("/api/v1.0/notes/:id", func(c echo.Context) error {
		id := c.Param("id")

		cachedNote, err := redisClient.Get(ctx, id).Result()
		if err == nil {
			return c.JSON(200, json.RawMessage(cachedNote))
		}


		if err := kafkaProducer.SendNoteMessage(ctx, "get", id, nil); err != nil {
			return c.JSON(500, map[string]string{"error": err.Error()})
		}

		res, err := awaitKafkaResponse(id)
		if err != nil {
			return c.JSON(504, map[string]string{"error": err.Error()})
		}

		redisClient.Set(ctx, id, res, 0)
		return c.JSON(200, json.RawMessage(res))
	})
}

func validateNote(dto model.NoteRequestTo) error {
	if len(dto.Content) < 2 || len(dto.Content) > 2048 {
		return errors.New("note content must be between 2 and 2048 characters")
	}
	return nil
}
