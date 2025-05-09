package handler

import (
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/handler/mark"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/handler/post"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/handler/topic"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/handler/writer"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/service"
	"github.com/gorilla/mux"
	"net/http"
)

const apiPrefix = "/api/v1.0"

func New(srv service.Service) http.Handler {
	h := mux.NewRouter()

	api := h.PathPrefix(apiPrefix).Subrouter()

	writer.New(srv.Writer).InitRoutes(api)
	topic.New(srv.Topic).InitRoutes(api)
	post.New(srv.Post).InitRoutes(api)
	mark.New(srv.Mark).InitRoutes(api)

	return h
}
