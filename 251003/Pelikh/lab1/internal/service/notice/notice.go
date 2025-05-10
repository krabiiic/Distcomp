package notice

import (
	"context"
	"lab1/internal/model"
	db "lab1/internal/storage/notice"
)

type service struct {
	db db.Repo
}

type Service interface {
	Create(ctx context.Context, req model.Notice) (model.Notice, error)
	GetList(ctx context.Context) ([]model.Notice, error)
	Get(ctx context.Context, id int64) (model.Notice, error)
	Update(ctx context.Context, req model.Notice) (model.Notice, error)
	Delete(ctx context.Context, id int64) error
}

func New(db db.Repo) Service {
	return service{
		db: db,
	}
}

func (s service) Create(ctx context.Context, req model.Notice) (model.Notice, error) {
	return s.db.Create(ctx, req)
}

func (s service) GetList(ctx context.Context) ([]model.Notice, error) {
	return s.db.GetList(ctx)
}

func (s service) Get(ctx context.Context, id int64) (model.Notice, error) {
	return s.db.Get(ctx, id)
}

func (s service) Update(ctx context.Context, req model.Notice) (model.Notice, error) {
	return s.db.Update(ctx, req)
}

func (s service) Delete(ctx context.Context, id int64) error {
	return s.db.Delete(ctx, id)
}
