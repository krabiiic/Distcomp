package creator

import (
	"context"
	"encoding/json"
	"errors"
	"lab2/internal/model"
	"lab2/internal/storage/creator"
	"net/http"
	"strconv"

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
	Create(ctx context.Context, cr model.Creator) (model.Creator, error)
	GetList(ctx context.Context) ([]model.Creator, error)
	Get(ctx context.Context, id int) (model.Creator, error)
	Update(ctx context.Context, cr model.Creator) (model.Creator, error)
	Delete(ctx context.Context, id int) error
}

func (c Handler) InitRoutes(r *mux.Router) {
	r.HandleFunc("/creators", c.getList).Methods(http.MethodGet)
	r.HandleFunc("/creators/{id}", c.get).Methods(http.MethodGet)
	r.HandleFunc("/creators", c.create).Methods(http.MethodPost)
	r.HandleFunc("/creators/{id}", c.delete).Methods(http.MethodDelete)
	r.HandleFunc("/creators", c.update).Methods(http.MethodPut)
}

func (c Handler) getList(w http.ResponseWriter, r *http.Request) {
	result, err := c.srv.GetList(r.Context())
	if err != nil {
		http.Error(w, "Failed to get result", http.StatusInternalServerError)
		return
	}

	if result == nil {
		result = make([]model.Creator, 0)
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(result); err != nil {
		http.Error(w, "Failed to encode result", http.StatusInternalServerError)
	}
}

func (c Handler) get(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(mux.Vars(r)["id"])
	if err != nil {
		http.Error(w, "Invalid ID format", http.StatusBadRequest)
		return
	}

	creator, err := c.srv.Get(r.Context(), id)
	if err != nil {
		http.Error(w, "Failed to get Handler", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(creator); err != nil {
		http.Error(w, "Failed to encode Handler", http.StatusInternalServerError)
	}
}

func (c Handler) create(w http.ResponseWriter, r *http.Request) {
	var req model.Creator

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, `{"error": "`+err.Error()+`"}`, http.StatusBadRequest)
		return
	}

	result, err := c.srv.Create(r.Context(), req)
	if err != nil {
		/*if errors.Is(err, creator.ErrLoginExists) {
			http.Error(w, `{"error": "Login already exists"}`, http.StatusForbidden)
			return
		}
		http.Error(w, "Failed to create creator", http.StatusInternalServerError)
		return*/
		http.Error(w, "{}", http.StatusForbidden)
		return
	}

	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusCreated)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func (c Handler) delete(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(mux.Vars(r)["id"])
	if err != nil {
		http.Error(w, `{"error": "Invalid ID format"}`, http.StatusBadRequest)
		return
	}

	ctx := r.Context()

	err = c.srv.Delete(ctx, id)
	if err != nil {
		if errors.Is(err, creator.ErrCreatorNotFound) {
			http.Error(w, `{"error": "Handler not found"}`, http.StatusNotFound)
			return
		}

		http.Error(w, `{"error": "Failed to delete Handler"}`, http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNoContent)
	_, _ = w.Write([]byte(`{"post": "Handler deleted successfully"}`))
}

func (c Handler) update(w http.ResponseWriter, r *http.Request) {
	var req model.Creator

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, `{"error": "`+err.Error()+`"}`, http.StatusBadRequest)
		return
	}

	result, err := c.srv.Update(r.Context(), req)
	if err != nil {
		if errors.Is(err, creator.ErrCreatorNotFound) {
			http.Error(w, "Handler not found", http.StatusNotFound)
			return
		}

		http.Error(w, "Failed to update Handler", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}
