package Post

import (
	"context"

	"github.com/gin-gonic/gin"
	"lab3/discussion/internal/model"
)

type PostService interface {
	GetPost(ctx context.Context, id int64) (model.Post, error)
	GetPosts(ctx context.Context) ([]model.Post, error)
	CreatePost(ctx context.Context, args model.Post) (model.Post, error)
	UpdatePost(ctx context.Context, args model.Post) (model.Post, error)
	DeletePost(ctx context.Context, id int64) error
}

type noticeHandler struct {
	notice PostService
}

func New(noticeSvc PostService) *noticeHandler {
	return &noticeHandler{
		notice: noticeSvc,
	}
}

func (h *noticeHandler) InitRoutes(router gin.IRouter) {
	v1 := router.Group("/v1.0")
	{
		v1.GET("/posts", h.List())
		v1.GET("/posts/:id", h.Get())
		v1.POST("/posts", h.Create())
		v1.DELETE("/posts/:id", h.Delete())
		v1.PUT("/posts", h.Update())
	}
}
