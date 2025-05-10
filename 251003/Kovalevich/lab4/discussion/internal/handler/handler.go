package handler

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"lab3/discussion/internal/handler/http/Post"
	"lab3/discussion/internal/service"
)

func New(svc service.Service) http.Handler {
	engine := gin.Default()

	router := engine.Group("/api")
	{
		Post.New(svc).InitRoutes(router)
	}

	return engine
}
