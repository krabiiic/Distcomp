package handler

import (
	"lab3/publisher/internal/handler/http"
	"lab3/publisher/internal/service"

	"github.com/gorilla/mux"
)

type Handler struct {
	HTTP *mux.Router
}

func New(srv service.Service) Handler {
	return Handler{
		HTTP: http.New(srv),
	}
}
