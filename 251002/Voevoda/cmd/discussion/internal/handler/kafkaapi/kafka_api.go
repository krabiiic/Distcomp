package kafkaapi

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/di"
	"github.com/strcarne/distributed-calculations/internal/entity/msg"
)

// Handler handles Kafka messages for the note service
type Handler struct {
	deps di.Container
}

// NewHandler creates a new Kafka API handler
func NewHandler(deps di.Container) *Handler {
	return &Handler{
		deps: deps,
	}
}

// Start begins consuming messages and processing them
func (h *Handler) Start(ctx context.Context) error {
	h.deps.Logger.Info("Starting Kafka consumer for note service")

	workersLimit := 1024
	semaphore := make(chan struct{}, workersLimit)

	for {
		select {
		case <-ctx.Done():
			return ctx.Err()
		default:
			readCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
			message, err := h.deps.Bus.Consume(readCtx)
			cancel()

			if err != nil {
				continue
			}

			semaphore <- struct{}{}
			go func(msg []byte) {
				defer func() { <-semaphore }()

				if err := h.processMessage(context.Background(), msg); err != nil {
					h.deps.Logger.Error("Failed to process message", "error", err)
				}
			}(message)
		}
	}
}

// processMessage handles a single message from Kafka
func (h *Handler) processMessage(ctx context.Context, message []byte) error {
	// Parse request
	var request msg.NoteRequest
	if err := json.Unmarshal(message, &request); err != nil {
		h.deps.Logger.Error("Failed to unmarshal request", "error", err)
		return err
	}

	h.deps.Logger.Info("Processing note request",
		"method", request.Method,
		"correlation_id", request.CorrelationID)

	// Prepare response
	response := msg.NoteResponse{
		CorrelationID: request.CorrelationID,
	}

	// Process request based on method
	switch request.Method {
	case msg.MethodCreate:
		note, err := h.deps.Services.Note.CreateNote(request.Note)
		if err != nil {
			errStr := err.Error()
			response.Error = &errStr
		} else {
			notePtr := &note
			response.Note = notePtr
		}

	case msg.MethodUpdate:
		err := h.deps.Services.Note.UpdateNote(request.Note)
		if err != nil {
			errStr := err.Error()
			response.Error = &errStr
		} else {
			notePtr := &request.Note
			response.Note = notePtr
		}

	case msg.MethodDelete:
		note, err := h.deps.Services.Note.DeleteNote(request.Note.ID)
		if err != nil {
			errStr := err.Error()
			response.Error = &errStr
		} else {
			notePtr := &note
			response.Note = notePtr
		}

	case msg.MethodGet:
		note, err := h.deps.Services.Note.GetNoteByID(request.Note.ID)
		if err != nil {
			errStr := err.Error()
			response.Error = &errStr
		} else {
			notePtr := &note
			response.Note = notePtr
		}

	case msg.MethodGetAll:
		h.deps.Logger.Info("Getting all notes")

		notes, err := h.deps.Services.Note.GetAllNotes()
		if err != nil {
			errStr := err.Error()
			response.Error = &errStr
			h.deps.Logger.Error("Failed to get all notes", "error", err)
		} else {
			response.Notes = notes
			h.deps.Logger.Info("All notes retrieved", "count", len(notes))
		}

	default:
		errStr := fmt.Sprintf("unknown method: %s", request.Method)
		response.Error = &errStr
	}

	// Marshal and send response
	responseBytes, err := json.Marshal(response)
	if err != nil {
		return err
	}

	h.deps.Logger.Info("Sending response", "correlation_id", response.CorrelationID)

	err = h.deps.Bus.Produce(ctx, responseBytes)
	if err != nil {
		h.deps.Logger.Error("Failed to produce response", "error", err)
		return err
	}

	h.deps.Logger.Info("Response sent", "correlation_id", response.CorrelationID)

	return nil
}
