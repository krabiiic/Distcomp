package http

import (
	"github.com/gorilla/mux"
	"lab3/publisher/internal/handler/http/creator"
	"lab3/publisher/internal/handler/http/marker"
	"lab3/publisher/internal/handler/http/notice"
	"lab3/publisher/internal/handler/http/topic"
	"lab3/publisher/internal/service"
)

const pathPrefix = "/api/v1.0"

func New(srv service.Service) *mux.Router {
	r := mux.NewRouter()
	api := r.PathPrefix(pathPrefix).Subrouter()

	creator := creator.New(srv.Creator)
	creator.InitRoutes(api)

	topic := topic.New(srv.Topic)
	topic.InitRoutes(api)

	notice := notice.New(srv.Notice)
	notice.InitRoutes(api)

	marker := marker.New(srv.Marker)
	marker.InitRoutes(api)

	return r
}
