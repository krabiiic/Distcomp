package notice

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	commentErr "lab1/internal/storage/notice"
	"net/http"
	"strconv"

	"lab1/internal/model"

	"github.com/gorilla/mux"
)

type Handler struct {
	srv Service
}

func New(srv Service) Handler {
	return Handler{
		srv: srv,
	}
}

type Service interface {
	Create(ctx context.Context, comment model.Notice) (model.Notice, error)
	GetList(ctx context.Context) ([]model.Notice, error)
	Get(ctx context.Context, id int64) (model.Notice, error)
	Update(ctx context.Context, comment model.Notice) (model.Notice, error)
	Delete(ctx context.Context, id int64) error
}

func (h Handler) InitRoutes(r *mux.Router) {
	r.HandleFunc("/notices", h.getList).Methods(http.MethodGet)
	r.HandleFunc("/notices", h.create).Methods(http.MethodPost)
	r.HandleFunc("/notices/{id}", h.get).Methods(http.MethodGet)
	r.HandleFunc("/notices/{id}", h.delete).Methods(http.MethodDelete)
	r.HandleFunc("/notices", h.update).Methods(http.MethodPut)
}

func (h Handler) getList(w http.ResponseWriter, r *http.Request) {
	comments, err := h.srv.GetList(r.Context())
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve comments: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(comments)
}

func (h Handler) create(w http.ResponseWriter, r *http.Request) {
	var req model.Notice

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, fmt.Sprintf("invalid input: %v", err), http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, fmt.Sprintf("validation error: %v", err), http.StatusBadRequest)
		return
	}

	result, err := h.srv.Create(r.Context(), req)
	if err != nil {
		if errors.Is(err, commentErr.ErrInvalidForeignKey) {
			http.Error(w, "tweetId does not exist in the topic table", http.StatusBadRequest)
			return
		}

		http.Error(w, fmt.Sprintf("failed to create req: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(result)
}

func (h Handler) get(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid result ID: %v", err), http.StatusBadRequest)
		return
	}

	result, err := h.srv.Get(r.Context(), id)
	if err != nil {
		if errors.Is(err, commentErr.ErrNoticeNotFound) {
			http.Error(w, "result not found", http.StatusNotFound)
			return
		}

		http.Error(w, fmt.Sprintf("failed to retrieve result: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (h Handler) delete(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid notice ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := h.srv.Delete(r.Context(), id); err != nil {
		if errors.Is(err, commentErr.ErrNoticeNotFound) {
			http.Error(w, "{}", http.StatusNotFound)
			return
		}

		http.Error(w, "{}", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNoContent)
	w.Write([]byte(`{}`))
}

func (h Handler) update(w http.ResponseWriter, r *http.Request) {
	var req model.Notice

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	result, err := h.srv.Update(r.Context(), req)
	if err != nil {
		if errors.Is(err, commentErr.ErrNoticeNotFound) {
			http.Error(w, "{}", http.StatusNotFound)
			return
		}

		http.Error(w, "{}", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}
