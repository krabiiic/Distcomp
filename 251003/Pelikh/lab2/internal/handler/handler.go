package handler

import (
	"lab2/internal/handler/creator"
	"lab2/internal/handler/marker"
	"lab2/internal/handler/notice"
	"lab2/internal/handler/topic"
	"lab2/internal/service"

	"github.com/gorilla/mux"
	"net/http"
)

const apiPrefix = "/api/v1.0"

func New(srv service.Service) http.Handler {
	h := mux.NewRouter()

	api := h.PathPrefix(apiPrefix).Subrouter()

	creator.New(srv.Creator).InitRoutes(api)
	topic.New(srv.Topic, srv.Marker).InitRoutes(api)
	notice.New(srv.Notice).InitRoutes(api)
	marker.New(srv.Marker).InitRoutes(api)

	return h
}
