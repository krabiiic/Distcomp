package http

import (
	"net/http"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/http/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/service"
	"github.com/gin-gonic/gin"
)

func New(svc service.Service) http.Handler {
	engine := gin.Default()

	router := engine.Group("/api")
	{
		notice.New(svc).InitRoutes(router)
	}

	return engine
}
