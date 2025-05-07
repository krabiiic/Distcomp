package infra

import (
	"context"
	"encoding/json"
	"errors"
	"log/slog"
	"sync/atomic"
	"time"

	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
	"github.com/strcarne/distributed-calculations/internal/entity/msg"
	"github.com/strcarne/distributed-calculations/pkg/container"
)

const (
	responseTTL      = 60 * time.Second
	cleanupInterval  = 2 * time.Minute
	kafkaReadTimeout = 5 * time.Second
)

// Bus handles communication with Kafka for message bus operations
type Bus struct {
	consumer      *kafka.Reader
	producer      *kafka.Writer
	noteResponses container.CacheTTL[uuid.UUID, msg.NoteResponse]

	isConsuming atomic.Bool

	logger *slog.Logger
}

// NewBus creates a new Bus instance configured with the given brokers and topics
func NewBus(cfg KafkaConfig, logger *slog.Logger) *Bus {
	consumer := kafka.NewReader(kafka.ReaderConfig{
		Brokers:     cfg.Brokers,
		Topic:       cfg.InTopic,
		StartOffset: kafka.LastOffset,
		GroupID:     uuid.New().String(),
		MinBytes:    1,
		MaxBytes:    10e6,
		MaxWait:     10 * time.Millisecond,
	})

	producer := kafka.NewWriter(kafka.WriterConfig{
		Brokers:      cfg.Brokers,
		Topic:        cfg.OutTopic,
		BatchSize:    1,
		BatchTimeout: 10 * time.Millisecond,
		RequiredAcks: int(kafka.RequireOne),
	})

	// Initialize a TTL noteResponses
	noteResponses := container.NewCacheTTL[uuid.UUID, msg.NoteResponse]()

	// Start cache cleanup in background
	go func() {
		ctx := context.Background()
		noteResponses.StartCleanUpByInterval(ctx, cleanupInterval)
	}()

	return &Bus{
		consumer:      consumer,
		producer:      producer,
		noteResponses: noteResponses,
		isConsuming:   atomic.Bool{},
		logger:        logger,
	}
}

func (s *Bus) Consume(ctx context.Context) ([]byte, error) {
	msg, err := s.consumer.ReadMessage(ctx)
	if err != nil {
		return nil, err
	}

	return msg.Value, nil
}

func (s *Bus) Produce(ctx context.Context, message []byte) error {
	return s.producer.WriteMessages(ctx, kafka.Message{Value: message})
}

func (s *Bus) Close() error {
	return errors.Join(s.consumer.Close(), s.producer.Close())
}

func (s *Bus) PostNoteRequest(ctx context.Context, request msg.NoteRequest) (uuid.UUID, error) {
	if request.CorrelationID == uuid.Nil {
		request.CorrelationID = uuid.New()
	}

	messageBytes, err := json.Marshal(request)
	if err != nil {
		return uuid.Nil, err
	}

	return request.CorrelationID, s.producer.WriteMessages(ctx, kafka.Message{
		Value: messageBytes,
	})
}

func (s *Bus) StartBackgroundConsumer(ctx context.Context) {

	if !s.isConsuming.CompareAndSwap(false, true) {
		s.logger.Warn("Background Kafka consumer already running, skipping")

		return
	}

	defer s.isConsuming.CompareAndSwap(true, false)

	s.logger.Info("Started background Kafka consumer for response cache", "topic", s.consumer.Config().Topic)

	for {
		select {
		case <-ctx.Done():
			slog.Info("Stopping background Kafka consumer")
			return

		default:
			readCtx, readCancel := context.WithTimeout(ctx, kafkaReadTimeout)
			kafkaMsg, err := s.consumer.ReadMessage(readCtx)
			readCancel()

			if err != nil {
				if errors.Is(err, context.DeadlineExceeded) {
					continue
				}
				s.logger.Error("Error reading from Kafka", "error", err)
				continue
			}

			var response msg.NoteResponse
			if err := json.Unmarshal(kafkaMsg.Value, &response); err != nil {
				slog.Error("Error unmarshalling response", "error", err)

				continue
			}

			if response.CorrelationID != uuid.Nil {
				s.noteResponses.Set(response.CorrelationID, response, responseTTL)
				slog.Info("Cached response", "correlation_id", response.CorrelationID)
			} else {
				slog.Error("Received response with no correlation ID", "correlation_id", response.CorrelationID)
			}

		}
	}
}

// GetNoteResponse retrieves a response with the given correlation ID from the cache
// If not found, returns an error
func (s *Bus) GetNoteResponse(ctx context.Context, correlationID uuid.UUID) (msg.NoteResponse, error) {

	if !s.isConsuming.Load() {
		return msg.NoteResponse{}, errors.New("background consumer not running, call StartBackgroundConsumer first")
	}

	if cachedResponse, found := s.noteResponses.Get(correlationID); found {
		return cachedResponse, nil
	}

	ticker := time.NewTicker(50 * time.Millisecond)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			if cachedResponse, found := s.noteResponses.Get(correlationID); found {
				return cachedResponse, nil
			}
		case <-ctx.Done():
			return msg.NoteResponse{}, ctx.Err()

		}
	}
}
