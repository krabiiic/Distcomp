package service

import (
	"distributedcomputing/model"
	"distributedcomputing/storage"
	"fmt"
)

type NoteService struct {
	store storage.IStorage[model.Note]
}

func NewNoteService(store storage.IStorage[model.Note]) *NoteService {
	return &NoteService{store: store}
}

func (s *NoteService) Create(dto model.NoteRequestTo) (model.NoteResponseTo, error) {
	note := model.NoteToModel(dto)
	fmt.Println("create", note)
	id, err := s.store.Create(note)
	fmt.Println("created", note)
	note.Id = id
	if err != nil {
		return model.NoteResponseTo{}, err
	}
	return model.NoteToDTO(note), nil
}

func (s *NoteService) Get(id int64) (model.NoteResponseTo, error) {
	note, err := s.store.Get(id)
	note.Id = id
	if err != nil {
		return model.NoteResponseTo{}, err
	}
	return model.NoteToDTO(note), nil
}

func (s *NoteService) Update(dto model.NoteRequestTo) error {
	note := model.NoteToModel(dto)
	return s.store.Update(note.Id, note)
}

func (s *NoteService) Delete(id int64) error {
	return s.store.Delete(id)
}

func (s *NoteService) GetAll() ([]model.NoteResponseTo, error) {
	creators, ids, err := s.store.GetAll()
	result := make([]model.NoteResponseTo, 0, len(creators))

	if err != nil {
		return result, err
	}

	for i, entity := range creators {
		entity.Id = ids[i]
		result = append(result, model.NoteToDTO(entity))
	}

	return result, nil
}
