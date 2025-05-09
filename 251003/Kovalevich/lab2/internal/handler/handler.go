package handler

import (
	"lab2/internal/handler/post"
	"lab2/internal/handler/writer"

	"lab2/internal/handler/mark"

	"lab2/internal/handler/topic"

	"lab2/internal/service"

	"github.com/gorilla/mux"
	"net/http"
)

const apiPrefix = "/api/v1.0"

func New(srv service.Service) http.Handler {
	h := mux.NewRouter()

	api := h.PathPrefix(apiPrefix).Subrouter()

	writer.New(srv.Writer).InitRoutes(api)
	topic.New(srv.Topic, srv.Mark).InitRoutes(api)
	post.New(srv.Post).InitRoutes(api)
	mark.New(srv.Mark).InitRoutes(api)

	return h
}
