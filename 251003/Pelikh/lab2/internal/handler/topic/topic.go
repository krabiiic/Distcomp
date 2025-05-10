package topic

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lab2/internal/model"
	"lab2/internal/service/marker"
	marker2 "lab2/internal/storage/marker"
	topic2 "lab2/internal/storage/topic"
	"math"
	"math/rand"
	"net/http"
	"strconv"
	"time"

	"github.com/gorilla/mux"
)

type topic struct {
	srv           Service
	markerService marker.Service
}

func New(srv Service, markerService marker.Service) topic {
	return topic{
		srv:           srv,
		markerService: markerService,
	}
}

type Service interface {
	Create(ctx context.Context, topic model.Topic) (model.Topic, error)
	GetList(ctx context.Context) ([]model.Topic, error)
	Get(ctx context.Context, id int64) (model.Topic, error)
	Update(ctx context.Context, topic model.Topic) (model.Topic, error)
	Delete(ctx context.Context, id int64) error
}

func (i topic) InitRoutes(r *mux.Router) {
	r.HandleFunc("/topics", i.getList).Methods(http.MethodGet)
	r.HandleFunc("/topics", i.create).Methods(http.MethodPost)
	r.HandleFunc("/topics/{id}", i.get).Methods(http.MethodGet)
	r.HandleFunc("/topics/{id}", i.deletet).Methods(http.MethodDelete)
	r.HandleFunc("/topics", i.update).Methods(http.MethodPut)
}

func (i topic) getList(w http.ResponseWriter, r *http.Request) {
	result, err := i.srv.GetList(r.Context())
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve result: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

func (i topic) create(w http.ResponseWriter, r *http.Request) {
	var req model.Topic

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if req.Markers != nil {
		rand.Seed(time.Now().UnixNano())

		for _, l := range req.Markers {
			_, err := i.markerService.Create(r.Context(), model.Marker{Name: l, ID: int64(rand.Intn(math.MaxInt) + 1)})
			if err != nil {
				if errors.Is(err, marker2.ErrConstraintsCheck) {
					http.Error(w, "{}", http.StatusBadRequest)
					return
				}

				http.Error(w, "{}", http.StatusBadRequest)
				return
			}
		}
	}

	result, err := i.srv.Create(r.Context(), req)
	if err != nil {
		if errors.Is(err, topic2.ErrDublicate) {
			http.Error(w, "{}", http.StatusForbidden)
			return
		}

		if errors.Is(err, topic2.ErrInvalidForeignKey) {
			http.Error(w, "{}", http.StatusBadRequest)
		}

		if errors.Is(err, topic2.ErrInvalidTopicData) {
			http.Error(w, "{}", http.StatusBadRequest)
		}

		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(result)
}

func (i topic) get(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid result ID: %v", err), http.StatusBadRequest)
		return
	}

	result, err := i.srv.Get(r.Context(), id)
	if err != nil {
		if errors.Is(err, topic2.ErrTopicNotFound) {
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

func (i topic) deletet(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.ParseInt(mux.Vars(r)["id"], 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid topic ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := i.srv.Delete(r.Context(), id); err != nil {
		if errors.Is(err, topic2.ErrTopicNotFound) {
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

func (i topic) update(w http.ResponseWriter, r *http.Request) {
	var req model.Topic

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := req.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	result, err := i.srv.Update(r.Context(), req)
	if err != nil {
		if errors.Is(err, topic2.ErrTopicNotFound) {
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
