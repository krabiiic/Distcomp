package notice

import (
	"context"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/model"
	"google.golang.org/grpc"
)

type noticeService interface {
	GetNotice(ctx context.Context, id int64) (model.Notice, error)
	ListNotices(ctx context.Context) ([]model.Notice, error)
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

func (n *noticeHandler) Register(reg grpc.ServiceRegistrar) {}
