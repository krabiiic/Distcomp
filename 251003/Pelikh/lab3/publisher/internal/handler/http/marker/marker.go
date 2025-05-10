package marker

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"

	markerModel "lab3/publisher/internal/model"
	markerDbModel "lab3/publisher/internal/storage/psql/marker"

	"github.com/gorilla/mux"
)

type Marker struct {
	srv MarkerService
}

func New(srv MarkerService) Marker {
	return Marker{
		srv: srv,
	}
}

type MarkerService interface {
	CreateMarker(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error)
	GetMarkers(ctx context.Context) ([]markerModel.Marker, error)
	GetMarkerByID(ctx context.Context, id int64) (markerModel.Marker, error)
	UpdateMarkerByID(ctx context.Context, marker markerModel.Marker) (markerModel.Marker, error)
	DeleteMarkerByID(ctx context.Context, id int64) error
}

func (m Marker) InitRoutes(r *mux.Router) {
	r.HandleFunc("/markers", m.getMarkersList).Methods(http.MethodGet)
	r.HandleFunc("/markers", m.createMarker).Methods(http.MethodPost)
	r.HandleFunc("/markers/{id}", m.getMarkerByID).Methods(http.MethodGet)
	r.HandleFunc("/markers/{id}", m.deleteMarkerByID).Methods(http.MethodDelete)
	r.HandleFunc("/markers", m.updateMarkerByID).Methods(http.MethodPut)
}

func (m Marker) getMarkersList(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	markers, err := m.srv.GetMarkers(ctx)
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to retrieve markers: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(markers); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode markers: %v", err), http.StatusInternalServerError)
	}
}

func (m Marker) createMarker(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var marker markerModel.Marker

	if err := json.NewDecoder(r.Body).Decode(&marker); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := marker.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	createdMarker, err := m.srv.CreateMarker(ctx, marker)
	if err != nil {
		if errors.Is(err, markerDbModel.ErrConstraintsCheck) {
			http.Error(w, "{}", http.StatusBadRequest)
			return
		}

		http.Error(w, "{}", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	if err := json.NewEncoder(w).Encode(createdMarker); err != nil {
		http.Error(w, "{}", http.StatusInternalServerError)
	}
}

func (m Marker) getMarkerByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	idStr := mux.Vars(r)["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid marker ID: %v", err), http.StatusBadRequest)
		return
	}

	marker, err := m.srv.GetMarkerByID(ctx, id)
	if err != nil {
		if err == markerDbModel.ErrNoticeNotFound {
			http.Error(w, "marker not found", http.StatusNotFound)
		} else {
			http.Error(w, fmt.Sprintf("failed to retrieve marker: %v", err), http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(marker); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode marker: %v", err), http.StatusInternalServerError)
	}
}

func (m Marker) deleteMarkerByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	idStr := mux.Vars(r)["id"]
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		http.Error(w, fmt.Sprintf("invalid marker ID: %v", err), http.StatusBadRequest)
		return
	}

	if err := m.srv.DeleteMarkerByID(ctx, id); err != nil {
		if err == markerDbModel.ErrNoticeNotFound {
			http.Error(w, "{}", http.StatusNotFound)
		} else {
			http.Error(w, "{}", http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusNoContent)
	w.Write([]byte(`{}`))
}

func (m Marker) updateMarkerByID(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var marker markerModel.Marker

	if err := json.NewDecoder(r.Body).Decode(&marker); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	if err := marker.Validate(); err != nil {
		http.Error(w, "{}", http.StatusBadRequest)
		return
	}

	updatedMarker, err := m.srv.UpdateMarkerByID(ctx, marker)
	if err != nil {
		if err == markerDbModel.ErrNoticeNotFound {
			http.Error(w, "{}", http.StatusNotFound)
		} else {
			http.Error(w, "{}", http.StatusInternalServerError)
		}
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(updatedMarker); err != nil {
		http.Error(w, fmt.Sprintf("failed to encode updated marker: %v", err), http.StatusInternalServerError)
	}
}
