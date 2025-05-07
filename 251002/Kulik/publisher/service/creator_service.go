package service

import (
	"distributedcomputing/model"
	"distributedcomputing/storage"
)

type CreatorService struct {
	store storage.IStorage[model.Creator]
}

func NewCreatorService(store storage.IStorage[model.Creator]) *CreatorService {
	return &CreatorService{store: store}
}

func (s *CreatorService) Create(dto model.CreatorRequestTo) (model.CreatorResponseTo, error) {
	creator := model.CreatorToModel(dto)
	id, err := s.store.Create(creator)
	creator.Id = id
	if err != nil {
		return model.CreatorResponseTo{}, err
	}
	return model.CreatorToDTO(creator), nil
}


func (s *CreatorService) GetAll() ([]model.CreatorResponseTo, error) {
	creators, ids, err := s.store.GetAll()
	result := make([]model.CreatorResponseTo, 0, len(creators))

	if err != nil {
		return result, err
	}

	for i, entity := range creators {
		entity.Id = ids[i]
		result = append(result, model.CreatorToDTO(entity))
	}

	return result, nil
}


func (s *CreatorService) Get(id int64) (model.CreatorResponseTo, error) {
	creator, err := s.store.Get(id)
	creator.Id = id
	if err != nil {
		return model.CreatorResponseTo{}, err
	}
	return model.CreatorToDTO(creator), nil
}


func (s *CreatorService) Update(dto model.CreatorRequestTo) error {
	creator := model.CreatorToModel(dto)
	return s.store.Update(creator.Id, creator)
}

func (s *CreatorService) Delete(id int64) error {
	return s.store.Delete(id)
}
