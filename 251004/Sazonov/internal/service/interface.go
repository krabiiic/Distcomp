package service

import (
	"context"

	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/model"
)

type Service interface {
	WriterService
	NewsService
	NoticeService
	LabelService
}

type WriterService interface {
	GetWriter(ctx context.Context, id int64) (model.Writer, error)
	ListWriters(ctx context.Context) ([]model.Writer, error)
	CreateWriter(ctx context.Context, args model.Writer) (model.Writer, error)
	UpdateWriter(ctx context.Context, args model.Writer) (model.Writer, error)
	DeleteWriter(ctx context.Context, id int64) error
}

type NewsService interface {
	GetNews(ctx context.Context, id int64) (model.News, error)
	ListNews(ctx context.Context) ([]model.News, error)
	CreateNews(ctx context.Context, args model.News) (model.News, error)
	UpdateNews(ctx context.Context, args model.News) (model.News, error)
	DeleteNews(ctx context.Context, id int64) error
}

type NoticeService interface {
	GetNotice(ctx context.Context, id int64) (model.Notice, error)
	ListNotices(ctx context.Context) ([]model.Notice, error)
	CreateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	UpdateNotice(ctx context.Context, args model.Notice) (model.Notice, error)
	DeleteNotice(ctx context.Context, id int64) error
}

type LabelService interface {
	GetLabel(ctx context.Context, id int64) (model.Label, error)
	ListLabels(ctx context.Context) ([]model.Label, error)
	CreateLabel(ctx context.Context, args model.Label) (model.Label, error)
	UpdateLabel(ctx context.Context, args model.Label) (model.Label, error)
	DeleteLabel(ctx context.Context, id int64) error
}
