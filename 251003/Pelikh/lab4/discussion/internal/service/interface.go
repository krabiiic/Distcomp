package service

import (
	"context"

	"lab3/discussion/internal/model"
)

type Service interface {
	NoticeService
}

type NoticeService interface {
	GetNotice(ctx context.Context, id int64) (model.Notice, error)
	GetNotices(ctx context.Context) ([]model.Notice, error)
	CreateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	UpdateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	DeleteNotice(ctx context.Context, id int64) error
}
