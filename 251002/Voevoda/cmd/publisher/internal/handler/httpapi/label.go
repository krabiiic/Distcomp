package httpapi

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di"
	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/internal/entity/server"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

func NewLabelRouter(deps di.Container) *chi.Mux {
	r := chi.NewRouter()

	r.Route("/", func(r chi.Router) {
		r.Get("/", wrapHandler(GetLabels, deps))
		r.Get("/{id}", wrapHandler(GetLabel, deps))
		r.Post("/", wrapHandler(CreateLabel, deps))
		r.Delete("/{id}", wrapHandler(DeleteLabel, deps))
		r.Put("/", wrapHandler(UpdateLabel, deps))
	})

	return r
}

func GetLabels(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	labels, err := deps.Services.Label.GetAllLabels()
	if err != nil {
		return server.NewError(err, http.StatusInternalServerError)
	}

	json.NewEncoder(w).Encode(labels)
	deps.Logger.Info("labels retrieved", "count", len(labels))

	return nil
}

func GetLabel(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	label, found, err := infra.CacheGet[entity.Label](deps.Cache, fmt.Sprintf("label_%d", id))
	if err != nil {
		return server.NewError(err, http.StatusInternalServerError)
	}

	if found {
		json.NewEncoder(w).Encode(label)
		deps.Logger.Info("label retrieved from cache", "label_id", id)

		return nil
	}

	label, err = deps.Services.Label.GetLabelByID(id)
	if err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	json.NewEncoder(w).Encode(label)
	deps.Logger.Info("label retrieved", "label_id", label.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("label_%d", label.ID), label); err != nil {
		deps.Logger.Error("failed to cache label", "label_id", label.ID, "error", err)
	}

	return nil
}

func CreateLabel(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var label entity.Label
	if err := json.NewDecoder(r.Body).Decode(&label); err != nil {
		return server.NewError(err, http.StatusForbidden)
	}

	label, err := deps.Services.Label.CreateLabel(label)
	if err != nil {
		return server.NewError(err, http.StatusForbidden)
	}

	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(label)
	deps.Logger.Info("label created", "label_id", label.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("label_%d", label.ID), label); err != nil {
		deps.Logger.Error("failed to cache label", "label_id", label.ID, "error", err)
	}

	return nil
}

func DeleteLabel(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	if _, err := deps.Services.Label.DeleteLabel(id); err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	w.WriteHeader(http.StatusNoContent)
	deps.Logger.Info("label deleted", "label_id", id)

	if err := deps.Cache.Delete(context.Background(), fmt.Sprintf("label_%d", id)); err != nil {
		deps.Logger.Error("failed to delete label from cache", "label_id", id, "error", err)
	}

	return nil
}

func UpdateLabel(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var label entity.Label
	if err := json.NewDecoder(r.Body).Decode(&label); err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	if err := deps.Services.Label.UpdateLabel(label); err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(label)
	deps.Logger.Info("label updated", "label_id", label.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("label_%d", label.ID), label); err != nil {
		deps.Logger.Error("failed to cache label", "label_id", label.ID, "error", err)
	}

	return nil
}
