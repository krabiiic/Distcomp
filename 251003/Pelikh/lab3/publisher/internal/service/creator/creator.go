package creator

import (
	"context"

	"lab3/publisher/internal/mapper"
	creator "lab3/publisher/internal/model"
	creatorModel "lab3/publisher/internal/model"
	dbcreatorModel "lab3/publisher/internal/storage/model"
)

type service struct {
	db CreatorDB
}

type CreatorDB interface {
	CreateCreator(ctx context.Context, cr dbcreatorModel.Creator) (dbcreatorModel.Creator, error)
	GetCreators(ctx context.Context) ([]dbcreatorModel.Creator, error)
	GetCreatorByID(ctx context.Context, id int64) (dbcreatorModel.Creator, error)
	UpdateCreatorByID(ctx context.Context, cr dbcreatorModel.Creator) (dbcreatorModel.Creator, error)
	DeleteCreatorByID(ctx context.Context, id int64) error
}

type CreatorService interface {
	CreateCreator(ctx context.Context, cr creatorModel.Creator) (creator.Creator, error)
	GetCreators(ctx context.Context) ([]creatorModel.Creator, error)
	GetCreatorByID(ctx context.Context, id int) (creatorModel.Creator, error)
	UpdateCreatorByID(ctx context.Context, cr creatorModel.Creator) (creatorModel.Creator, error)
	DeleteCreatorByID(ctx context.Context, id int) error
}

func New(db CreatorDB) CreatorService {
	return &service{
		db: db,
	}
}

func (s *service) CreateCreator(ctx context.Context, cr creatorModel.Creator) (creator.Creator, error) {
	mappedData, err := s.db.CreateCreator(ctx, mapper.MapCreatorToModel(cr))
	if err != nil {
		return creator.Creator{}, err
	}

	return mapper.MapModelToCreator(mappedData), nil
}

func (s *service) GetCreators(ctx context.Context) ([]creatorModel.Creator, error) {
	creators, err := s.db.GetCreators(ctx)
	if err != nil {
		return nil, err
	}

	var mappedCreators []creatorModel.Creator

	for _, creator := range creators {
		mappedCreators = append(mappedCreators, mapper.MapModelToCreator(creator))
	}

	return mappedCreators, nil
}

func (s *service) GetCreatorByID(ctx context.Context, id int) (creatorModel.Creator, error) {
	creator, err := s.db.GetCreatorByID(ctx, int64(id))
	if err != nil {
		return creatorModel.Creator{}, err
	}

	return mapper.MapModelToCreator(creator), nil
}

func (s *service) UpdateCreatorByID(ctx context.Context, cr creatorModel.Creator) (creatorModel.Creator, error) {
	creator, err := s.db.UpdateCreatorByID(ctx, mapper.MapCreatorToModel(cr))
	if err != nil {
		return creatorModel.Creator{}, err
	}

	return mapper.MapModelToCreator(creator), nil
}

func (s *service) DeleteCreatorByID(ctx context.Context, id int) error {
	return s.db.DeleteCreatorByID(ctx, int64(id))
}
