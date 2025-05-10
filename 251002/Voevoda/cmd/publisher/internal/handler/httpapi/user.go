package httpapi

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"
	"github.com/jackc/pgx/v5/pgconn"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di"
	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/internal/entity/server"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

func NewUserRouter(deps di.Container) *chi.Mux {
	r := chi.NewRouter()

	r.Route("/", func(r chi.Router) {
		r.Get("/", wrapHandler(GetUsers, deps))
		r.Get("/{id}", wrapHandler(GetUser, deps))
		r.Post("/", wrapHandler(CreateUser, deps))
		r.Delete("/{id}", wrapHandler(DeleteUser, deps))
		r.Put("/", wrapHandler(UpdateUser, deps))
	})

	return r
}

func GetUsers(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	users, err := deps.Services.User.GetAllUsers()
	if err != nil {
		return server.NewError(err, http.StatusInternalServerError)
	}

	json.NewEncoder(w).Encode(users)
	deps.Logger.Info("users retrieved", "count", len(users))

	return nil
}

func GetUser(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")

	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	user, found, err := infra.CacheGet[entity.User](deps.Cache, fmt.Sprintf("user_%d", id))
	if err != nil {
		return server.NewError(err, http.StatusInternalServerError)
	}

	if found {
		json.NewEncoder(w).Encode(user)
		deps.Logger.Info("user retrieved from cache", "user_id", id)
		return nil
	}

	user, err = deps.Services.User.GetUserByID(id)
	if err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	json.NewEncoder(w).Encode(user)
	deps.Logger.Info("user retrieved", "user_id", user.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("user_%d", user.ID), user); err != nil {
		deps.Logger.Error("failed to cache user", "user_id", user.ID, "error", err)
	}

	return nil
}

func CreateUser(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var user entity.User
	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	user, err := deps.Services.User.CreateUser(user)
	if err != nil {
		pgErr := &pgconn.PgError{}
		if errors.As(err, &pgErr) && pgErr.Code == "23505" {
			return server.NewError(err, http.StatusForbidden)
		}

		return server.NewError(err, http.StatusBadRequest)
	}

	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(user)
	deps.Logger.Info("user created", "user_id", user.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("user_%d", user.ID), user); err != nil {
		deps.Logger.Error("failed to cache user", "user_id", user.ID, "error", err)
	}

	return nil
}

func DeleteUser(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	idStr := chi.URLParam(r, "id")

	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	if _, err := deps.Services.User.DeleteUser(id); err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	w.WriteHeader(http.StatusNoContent)
	deps.Logger.Info("user deleted", "user_id", id)

	if err := deps.Cache.Delete(context.Background(), fmt.Sprintf("user_%d", id)); err != nil {
		deps.Logger.Error("failed to delete user from cache", "user_id", id, "error", err)
	}

	return nil
}

func UpdateUser(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error {
	var user entity.User
	if err := json.NewDecoder(r.Body).Decode(&user); err != nil {
		return server.NewError(err, http.StatusBadRequest)
	}

	if err := deps.Services.User.UpdateUser(user); err != nil {
		return server.NewError(err, http.StatusNotFound)
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(user)
	deps.Logger.Info("user updated", "user_id", user.ID)

	if err := infra.CacheSet(deps.Cache, fmt.Sprintf("user_%d", user.ID), user); err != nil {
		deps.Logger.Error("failed to cache user", "user_id", user.ID, "error", err)
	}

	return nil
}
