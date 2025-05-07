package httpapi

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di"
	"github.com/strcarne/distributed-calculations/internal/entity/server"
)

type handlerFunc func(w http.ResponseWriter, r *http.Request, deps di.Container) *server.Error

func RegisterV1(router chi.Router, deps di.Container) {
	router.Route("/api/v1.0", func(r chi.Router) {
		r.Mount("/users", NewUserRouter(deps))
		r.Mount("/labels", NewLabelRouter(deps))
		r.Mount("/tweets", NewTweetRouter(deps))
		r.Mount("/notes", NewNoteRouter(deps))
	})
}

func wrapHandler(handler handlerFunc, deps di.Container) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if serverError := handler(w, r, deps); serverError != nil {
			response := map[string]string{
				"error": serverError.Error,
			}

			payload, err := json.Marshal(response)
			if err != nil {
				deps.Logger.Error("failed to marshal response", "error", err)
				http.Error(w, err.Error(), http.StatusInternalServerError)

				return
			}

			deps.Logger.Error("failed to write response", "error", serverError.Error)
			http.Error(w, string(payload), serverError.Code)
		}
	}
}
