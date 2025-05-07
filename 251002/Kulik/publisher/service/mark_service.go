package service

import (
	"distributedcomputing/model"
	"distributedcomputing/storage"
)

type MarkService struct {
	store storage.IStorage[model.Mark]
}

func NewMarkService(store storage.IStorage[model.Mark]) *MarkService {
	return &MarkService{store: store}
}

func (s *MarkService) Create(dto model.MarkRequestTo) (model.MarkResponseTo, error) {
	mark := model.MarkToModel(dto)
	id, err := s.store.Create(mark)
	mark.Id = id
	if err != nil {
		return model.MarkResponseTo{}, err
	}
	return model.MarkToDTO(mark), nil
}

func (s *MarkService) Get(id int64) (model.MarkResponseTo, error) {
	mark, err := s.store.Get(id)
	mark.Id = id
	if err != nil {
		return model.MarkResponseTo{}, err
	}
	return model.MarkToDTO(mark), nil
}

func (s *MarkService) Update(dto model.MarkRequestTo) error {
	mark := model.MarkToModel(dto)
	return s.store.Update(mark.Id, mark)
}

func (s *MarkService) Delete(id int64) error {
	return s.store.Delete(id)
}

func (s *MarkService) GetAll() ([]model.MarkResponseTo, error) {
	creators, ids, err := s.store.GetAll()
	result := make([]model.MarkResponseTo, 0, len(creators))

	if err != nil {
		return result, err
	}

	for i, entity := range creators {
		entity.Id = ids[i]
		result = append(result, model.MarkToDTO(entity))
	}

	return result, nil
}
