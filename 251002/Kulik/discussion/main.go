package main

import (
	"context"
	"distributedcomputing/controllers"
	"distributedcomputing/model"
	"distributedcomputing/service"
	"distributedcomputing/storage"
	"encoding/json"
	"fmt"

	// "fmt"
	"log"
	"strconv"

	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/segmentio/kafka-go"

	// "go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

// --------- Kafka Topics & Config ---------
const (
	kafkaBroker = "localhost:9094"
	inTopic     = "InTopic"
	outTopic    = "OutTopic"
)

// --------- Mongo Config ---------
const (
	mongoURI        = "mongodb://localhost:27017"
	databaseName    = "notes_db"
	collectionName  = "notes"
	mongoTimeoutSec = 10
)

type Note struct {
	Id      int64  `json:"id" bson:"id"`
	Title   string `json:"title" bson:"title"`
	Content string `json:"content" bson:"content"`
}

type NoteMessage struct {
	Action string `json:"action"`
	ID     string `json:"id,omitempty"`
	Data   string `json:"data,omitempty"`
}

func main() {

	clientOptions := options.Client().ApplyURI("mongodb://localhost:27017")
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}

	defer client.Disconnect(context.TODO())

	ctx := context.Background()


	dbName := "notesDB"
	collectionName := "notes"

	storage := storage.NewNoteStorage(client, dbName, collectionName)
	noteService := service.NewNoteService(storage)
	noteController := controllers.NewNoteController(noteService)

	e := echo.New()
	e.Use(middleware.Recover())

	e.POST("/api/v1.0/notes", noteController.Create)
	e.PUT("/api/v1.0/notes", noteController.Update)
	e.GET("/api/v1.0/notes", noteController.GetAll)
	e.DELETE("/api/v1.0/notes/:id", noteController.Delete)
	e.GET("/api/v1.0/notes/:id", noteController.Get)


	go func() {
		reader := kafka.NewReader(kafka.ReaderConfig{
			Brokers:        []string{kafkaBroker},
			Topic:          inTopic,
			GroupID:        "note-service-group",
			MinBytes:       1,                     // Return as soon as at least 1 byte is available
			MaxBytes:       10e6,                  // Max size for a batch of messages
			MaxWait:        10 * time.Millisecond, // Return quickly even if not full
			ReadBatchTimeout: 100 * time.Millisecond,
		})
		defer reader.Close()
	
		writer := &kafka.Writer{
			Addr:          kafka.TCP(kafkaBroker),
			Topic:         outTopic,
			Balancer:      &kafka.LeastBytes{},       // Use least-bytes for fair load balancing
			BatchSize:     1,                         // Send each message immediately
			BatchTimeout:  10 * time.Millisecond,     // Flush batch quickly even if not full
			AllowAutoTopicCreation: true,             // Optional: allows auto-creating topics if needed
		}
		defer writer.Close()
	
		log.Println("Note Service is listening for Kafka messages...")
	
		for {
			for {
				m, err := reader.ReadMessage(ctx)
				if err != nil {
					log.Printf("Kafka read error: %v", err)
					continue
				}
		
				var msg NoteMessage
				if err := json.Unmarshal(m.Value, &msg); err != nil {
					log.Printf("Unmarshal error: %v", err)
					continue
				}
		
				log.Printf("Received message: %+v", msg)
		
				var response interface{}
				fmt.Println(msg)
				switch msg.Action {
				case "create":
					var note model.NoteRequestTo
					if err := json.Unmarshal([]byte(msg.Data), &note); err != nil {
						response = errorMsg("Invalid create data")
						break
					}
					response, err = noteService.Create(note)
					if err != nil {
						response = errorMsg("Mongo insert failed: " + err.Error())
					}
		
				case "update":
					var note model.NoteRequestTo
					if err := json.Unmarshal([]byte(msg.Data), &note); err != nil {
						response = errorMsg("Invalid update data")
						break
					}
					err = noteService.Update(note)
					if err != nil {
						response = errorMsg("Mongo update failed: " + err.Error())
					} else {
						response = note
					}
		
				case "delete":
					id, _ := strconv.ParseInt(msg.ID, 10, 64)
					err = noteService.Delete(id)
					if err != nil {
						response = errorMsg("Mongo delete failed: " + err.Error())
					} else {
						response = map[string]string{"status": "deleted"}
					}
		
				case "get":
					id, _ := strconv.ParseInt(msg.ID, 10, 64)
					
					note, err := noteService.Get(id)
					if err != nil {
						response = errorMsg("Note not found")
					} else {
						response = note
					}
		
				case "get_all":
					fmt.Println(" started processinglalal")
					notes, err := noteService.GetAll()
					if err != nil {
						response = errorMsg("Note not found")
					} else {
						response = notes
					}
					fmt.Println(notes)
		
				default:
					response = errorMsg("Unknown action")
				}
		
				respBytes, _ := json.Marshal(NoteMessage{
					ID:   msg.ID,
					Data: string(mustJSON(response)),
				})
				err = writer.WriteMessages(ctx, kafka.Message{Key: []byte(msg.ID), Value: respBytes})
				if err != nil {
					log.Printf("Failed to write response: %v", err)
				}
			}
		}
	}()

	e.Logger.Fatal(e.Start(":24130"))

}

func errorMsg(msg string) map[string]string {
	return map[string]string{"error": msg}
}

func mustJSON(v interface{}) []byte {
	b, _ := json.Marshal(v)
	return b
}
