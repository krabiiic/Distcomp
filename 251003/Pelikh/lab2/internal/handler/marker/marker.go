package marker

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lab2/internal/model"
	marker2 "lab2/internal/storage/marker"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

type marker struct {
	srv Service
}

func New(srv Service) marker {
	return marker{
		srv: srv,
	}
}

type Service interface {
	Create(ctx context.Context, label model.Marker) (model.Marker, error)
	GetList(ctx context.Context) ([]model.Marker, error)
	Get(ctx context.Context, id int64) (model.Marker, error)
	Update(ctx context.Context, label model.Marker) (model.Marker, error)
	Delete(ctx context.Context, id int64) error
}

func (m marker) InitRoutes(r *mux.Router) {
	r.HandleFunc("/markers", m.getList).Methods(http.MethodGet)
	r.HandleFunc("/markers", m.create).Methods(http.MethodPost)
	r.HandleFunc("/markers/{id}", m.get).Methods(http.MethodGet)
	r.HandleFunc("/markers/{id}", m.delete).Methods(http.MethodDelete)
	r.HandleFunc("/markers", m.update).Methods(http.MethodPut)
}

func (m marker) getList(w http.ResponseWriter, r *http.Request) {
	result, err := m.srv.GetList(r.Context())
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve result: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (m marker) create(w http.ResponseWriter, r *http.Request) {
	var req model.Marker

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	result, err := m.srv.Create(r.Context(), req)
	if err != nil {
		if errors.Is(err, marker2.ErrConstraintsCheck) {
			http.Error(w, "{}", http.StatusBadRequest)
			return
		}

		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(result)
}

func (m marker) get(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid result ID: %v", err), http.StatusBadRequest)
		return
	}

	result, err := m.srv.Get(r.Context(), id)
	if err != nil {
		if errors.Is(err, marker2.ErrNoticeNotFound) {
			http.Error(w, "result not found", http.StatusNotFound)
			return
		}

		http.Error(w, fmt.Sprintf("failed to retrieve notice: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (m marker) delete(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid marker ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := m.srv.Delete(r.Context(), id); err != nil {
		if errors.Is(err, marker2.ErrNoticeNotFound) {
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

func (m marker) update(w http.ResponseWriter, r *http.Request) {
	var req model.Marker

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
		if errors.Is(err, marker2.ErrNoticeNotFound) {
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
