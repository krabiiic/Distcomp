package creator

import (
	"context"
	"lab2/internal/model"
	db "lab2/internal/storage/creator"
)

type service struct {
	db db.Repo
}

type Service interface {
	Create(ctx context.Context, req model.Creator) (model.Creator, error)
	GetList(ctx context.Context) ([]model.Creator, error)
	Get(ctx context.Context, id int) (model.Creator, error)
	Update(ctx context.Context, req model.Creator) (model.Creator, error)
	Delete(ctx context.Context, id int) error
}

func New(db db.Repo) Service {
	return service{
		db: db,
	}
}

func (s service) Create(ctx context.Context, req model.Creator) (model.Creator, error) {
	return s.db.Create(ctx, req)
}

func (s service) GetList(ctx context.Context) ([]model.Creator, error) {
	return s.db.GetList(ctx)
}

func (s service) Get(ctx context.Context, id int) (model.Creator, error) {
	return s.db.Get(ctx, int64(id))
}

func (s service) Update(ctx context.Context, req model.Creator) (model.Creator, error) {
	return s.db.Update(ctx, req)
}

func (s service) Delete(ctx context.Context, id int) error {
	return s.db.Delete(ctx, int64(id))
}
