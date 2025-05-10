package storage

import (
	"context"

	"lab3/discussion/internal/model"
)

type Repository interface {
	NoticeRepo

	Close()
}

type NoticeRepo interface {
	GetPost(ctx context.Context, id int64) (model.Post, error)
	GetPosts(ctx context.Context) ([]model.Post, error)
	CreatePost(ctx context.Context, args model.Post) (model.Post, error)
	UpdatePost(ctx context.Context, args model.Post) (model.Post, error)
	DeletePost(ctx context.Context, id int64) error
}
