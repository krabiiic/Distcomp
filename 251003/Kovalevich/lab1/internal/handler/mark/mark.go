package mark

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	labelErr "github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage/mark"
	"net/http"
	"strconv"

	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/model"
	"github.com/gorilla/mux"
)

type label struct {
	srv Service
}

func New(srv Service) label {
	return label{
		srv: srv,
	}
}

type Service interface {
	Create(ctx context.Context, label model.Mark) (model.Mark, error)
	GetList(ctx context.Context) ([]model.Mark, error)
	Get(ctx context.Context, id int64) (model.Mark, error)
	Update(ctx context.Context, label model.Mark) (model.Mark, error)
	Delete(ctx context.Context, id int64) error
}

func (m label) InitRoutes(r *mux.Router) {
	r.HandleFunc("/marks", m.getList).Methods(http.MethodGet)
	r.HandleFunc("/marks", m.create).Methods(http.MethodPost)
	r.HandleFunc("/marks/{id}", m.get).Methods(http.MethodGet)
	r.HandleFunc("/marks/{id}", m.delete).Methods(http.MethodDelete)
	r.HandleFunc("/marks", m.update).Methods(http.MethodPut)
}

func (m label) getList(w http.ResponseWriter, r *http.Request) {
	result, err := m.srv.GetList(r.Context())
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve result: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (m label) create(w http.ResponseWriter, r *http.Request) {
	var req model.Mark

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, fmt.Sprintf("invalid input: %v", err), http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, fmt.Sprintf("validation error: %v", err), http.StatusBadRequest)
		return
	}

	result, err := m.srv.Create(r.Context(), req)
	if err != nil {
		if errors.Is(err, labelErr.ErrConstraintsCheck) {
			http.Error(w, "tweetId does not exist in the req table", http.StatusBadRequest)
			return
		}

		http.Error(w, fmt.Sprintf("failed to create req: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(result)
}

func (m label) get(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid result ID: %v", err), http.StatusBadRequest)
		return
	}

	result, err := m.srv.Get(r.Context(), id)
	if err != nil {
		if errors.Is(err, labelErr.ErrCommentNotFound) {
			http.Error(w, "result not found", http.StatusNotFound)
			return
		}

		http.Error(w, fmt.Sprintf("failed to retrieve post: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (m label) delete(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid mark ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := m.srv.Delete(r.Context(), id); err != nil {
		if errors.Is(err, labelErr.ErrCommentNotFound) {
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

func (m label) update(w http.ResponseWriter, r *http.Request) {
	var req model.Mark

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	result, err := m.srv.Update(r.Context(), req)
	if err != nil {
		if errors.Is(err, labelErr.ErrCommentNotFound) {
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
