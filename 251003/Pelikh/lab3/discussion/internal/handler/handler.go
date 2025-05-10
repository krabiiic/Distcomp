package handler

import (
	//"lab3/discussion/internal/storage/cassandra/notice"
	//Notice "lab3/discussion/internal/storage/cassandra/notice"
	"net/http"

	"github.com/gin-gonic/gin"
	"lab3/discussion/internal/handler/http/Notice"
	"lab3/discussion/internal/service"
)

func New(svc service.Service) http.Handler {
	engine := gin.Default()

	router := engine.Group("/api")
	{
		notice.New(svc).InitRoutes(router)
	}

	return engine
}
