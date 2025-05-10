package notice

import (
	"context"

	"github.com/gin-gonic/gin"
	"lab3/discussion/internal/model"
)

type noticeService interface {
	GetNotice(ctx context.Context, id int64) (model.Notice, error)
	GetNotices(ctx context.Context) ([]model.Notice, error)
	CreateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	UpdateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	DeleteNotice(ctx context.Context, id int64) error
}

type noticeHandler struct {
	notice noticeService
}

func New(noticeSvc noticeService) *noticeHandler {
	return &noticeHandler{
		notice: noticeSvc,
	}
}

func (h *noticeHandler) InitRoutes(router gin.IRouter) {
	v1 := router.Group("/v1.0")
	{
		v1.GET("/notices", h.List())
		v1.GET("/notices/:id", h.Get())
		v1.POST("/notices", h.Create())
		v1.DELETE("/notices/:id", h.Delete())
		v1.PUT("/notices", h.Update())
	}
}
