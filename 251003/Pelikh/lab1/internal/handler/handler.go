package handler

import (
	"lab1/internal/handler/creator"
	"lab1/internal/handler/marker"
	"lab1/internal/handler/notice"
	"lab1/internal/handler/topic"
	"lab1/internal/service"
	"net/http"

	"github.com/gorilla/mux"
)

const apiPrefix = "/api/v1.0"

func New(srv service.Service) http.Handler {
	h := mux.NewRouter()

	api := h.PathPrefix(apiPrefix).Subrouter()

	creator.New(srv.Creator).InitRoutes(api)
	topic.New(srv.Topic).InitRoutes(api)
	notice.New(srv.Notice).InitRoutes(api)
	marker.New(srv.Marker).InitRoutes(api)

	return h
}
