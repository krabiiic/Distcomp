package service

import (
	"distributedcomputing/model"
	"distributedcomputing/storage"
)

type StoryService struct {
	store storage.IStorage[model.Story]
}

func NewStoryService(store storage.IStorage[model.Story]) *StoryService {
	return &StoryService{store: store}
}


func (s *StoryService) Create(dto model.StoryRequestTo) (model.StoryResponseTo, error) {
	story := model.StoryToModel(dto)
	id, err := s.store.Create(story)
	story.Id = id
	if err != nil {
		return model.StoryResponseTo{}, err
	}
	return model.StoryToDTO(story), nil
}

func (s *StoryService) Get(id int64) (model.StoryResponseTo, error) {
	story, err := s.store.Get(id)
	story.Id = id
	if err != nil {
		return model.StoryResponseTo{}, err
	}
	return model.StoryToDTO(story), nil
}

func (s *StoryService) Update(dto model.StoryRequestTo) error {
	story := model.StoryToModel(dto)
	return s.store.Update(story.Id, story)
}

func (s *StoryService) Delete(id int64) error {
	return s.store.Delete(id)
}

func (s *StoryService) GetAll() ([]model.StoryResponseTo, error) {
	creators, ids, err := s.store.GetAll()
	result := make([]model.StoryResponseTo, 0, len(creators))

	if err != nil {
		return result, err
	}

	for i, entity := range creators {
		entity.Id = ids[i]
		result = append(result, model.StoryToDTO(entity))
	}

	return result, nil
}
