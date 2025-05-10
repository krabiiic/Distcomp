package handler

import (
	"net/http"

	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/handler/http/label"
	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/handler/http/news"
	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/handler/http/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/handler/http/writer"
	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/service"
	"github.com/gin-gonic/gin"
)

func New(svc service.Service) http.Handler {
	router := gin.Default()

	r := router.Group("/api")
	{
		writer.New(svc).InitRoutes(r)
		notice.New(svc).InitRoutes(r)
		news.New(svc).InitRoutes(r)
		label.New(svc).InitRoutes(r)
	}

	return router
}
