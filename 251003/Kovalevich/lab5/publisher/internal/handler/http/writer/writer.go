package writer

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"strconv"

	WriterModel "lab3/publisher/internal/model"
	WriterDbModel "lab3/publisher/internal/storage/psql/writer"

	"github.com/gorilla/mux"
)

type Writer struct {
	srv WriterService
}

func New(srv WriterService) Writer {
	return Writer{
		srv: srv,
	}
}

type WriterService interface {
	CreateWriter(ctx context.Context, cr WriterModel.Writer) (WriterModel.Writer, error)
	GetWriters(ctx context.Context) ([]WriterModel.Writer, error)
	GetWriterByID(ctx context.Context, id int) (WriterModel.Writer, error)
	UpdateWriterByID(ctx context.Context, cr WriterModel.Writer) (WriterModel.Writer, error)
	DeleteWriterByID(ctx context.Context, id int) error
}

func (c Writer) InitRoutes(r *mux.Router) {
	r.HandleFunc("/writers", c.getWritersList).Methods(http.MethodGet)
	r.HandleFunc("/writers/{id}", c.getWriterByID).Methods(http.MethodGet)
	r.HandleFunc("/writers", c.createWriter).Methods(http.MethodPost)
	r.HandleFunc("/writers/{id}", c.deleteWriterByID).Methods(http.MethodDelete)
	r.HandleFunc("/writers", c.updateWriterByID).Methods(http.MethodPut)
}

func (c Writer) getWritersList(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	Writers, err := c.srv.GetWriters(ctx)
	if err != nil {
		http.Error(w, "Failed to get Writers", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(Writers); err != nil {
		http.Error(w, "Failed to encode Writers", http.StatusInternalServerError)
	}
}

func (c Writer) getWriterByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, "Invalid ID format", http.StatusBadRequest)
		return
	}

	ctx := r.Context()

	Writer, err := c.srv.GetWriterByID(ctx, id)
	if err != nil {
		http.Error(w, "Failed to get Writer", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(Writer); err != nil {
		http.Error(w, "Failed to encode Writer", http.StatusInternalServerError)
	}
}

func (c Writer) createWriter(w http.ResponseWriter, r *http.Request) {
	var cr WriterModel.Writer
	if err := json.NewDecoder(r.Body).Decode(&cr); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := cr.Validate(); err != nil {
		http.Error(w, `{"error": "`+err.Error()+`"}`, http.StatusBadRequest)
		return
	}

	ctx := r.Context()

	Writer, err := c.srv.CreateWriter(ctx, cr)
	if err != nil {
		if errors.Is(err, WriterDbModel.ErrLoginExists) {
			http.Error(w, "{}", http.StatusForbidden)
			return
		}

		http.Error(w, "Failed to create Writer", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusCreated)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(Writer)
}

func (c Writer) deleteWriterByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	id, err := strconv.Atoi(idStr)
	if err != nil {
		http.Error(w, `{"error": "Invalid ID format"}`, http.StatusBadRequest)
		return
	}

	ctx := r.Context()

	err = c.srv.DeleteWriterByID(ctx, id)
	if err != nil {
		if errors.Is(err, WriterDbModel.ErrWriterNotFound) {
			http.Error(w, `{"error": "Writer not found"}`, http.StatusNotFound)
			return
		}
		http.Error(w, `{"error": "Failed to delete Writer"}`, http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNoContent)
	_, _ = w.Write([]byte(`{"post": "Writer deleted successfully"}`))
}

func (c Writer) updateWriterByID(w http.ResponseWriter, r *http.Request) {
	var cr WriterModel.Writer
	if err := json.NewDecoder(r.Body).Decode(&cr); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := cr.Validate(); err != nil {
		http.Error(w, `{"error": "`+err.Error()+`"}`, http.StatusBadRequest)
		return
	}

	ctx := r.Context()

	updatedWriter, err := c.srv.UpdateWriterByID(ctx, cr)
	if err != nil {
		if errors.Is(err, WriterDbModel.ErrWriterNotFound) {
			http.Error(w, "Writer not found", http.StatusNotFound)
			return
		}
		http.Error(w, "Failed to update Writer", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(updatedWriter); err != nil {
		http.Error(w, "Failed to encode updated Writer", http.StatusInternalServerError)
	}
}
