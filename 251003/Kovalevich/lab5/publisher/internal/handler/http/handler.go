package http

import (
	"github.com/gorilla/mux"
	"lab3/publisher/internal/handler/http/mark"
	"lab3/publisher/internal/handler/http/post"
	"lab3/publisher/internal/handler/http/topic"
	"lab3/publisher/internal/handler/http/writer"
	"lab3/publisher/internal/service"
)

const pathPrefix = "/api/v1.0"

func New(srv service.Service) *mux.Router {
	r := mux.NewRouter()
	api := r.PathPrefix(pathPrefix).Subrouter()

	writer := writer.New(srv.Writer)
	writer.InitRoutes(api)

	topic := topic.New(srv.Topic)
	topic.InitRoutes(api)

	post := post.New(srv.Post)
	post.InitRoutes(api)

	mark := mark.New(srv.Mark)
	mark.InitRoutes(api)

	return r
}
