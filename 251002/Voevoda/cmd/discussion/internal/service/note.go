package service

import (
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type Note struct {
	repo NoteRepository
}

type NoteRepository interface {
	CreateNote(note entity.Note) (int64, error)
	DeleteNote(id int64) (entity.Note, error)
	GetAllNotes() ([]entity.Note, error)
	GetNoteByID(id int64) (entity.Note, error)
	UpdateNote(note entity.Note) error
}

func NewNote(repo NoteRepository) Note {
	return Note{
		repo: repo,
	}
}

func (u Note) CreateNote(note entity.Note) (entity.Note, error) {
	id, err := u.repo.CreateNote(note)
	if err != nil {
		return entity.Note{}, err
	}

	note.ID = id

	return note, nil
}

func (u Note) DeleteNote(id int64) (entity.Note, error) {
	note, err := u.repo.DeleteNote(id)
	if err != nil {
		return entity.Note{}, err
	}

	return note, nil
}

func (u Note) GetAllNotes() ([]entity.Note, error) {
	notes, err := u.repo.GetAllNotes()
	if err != nil {
		return nil, err
	}

	return notes, nil
}

func (u Note) GetNoteByID(id int64) (entity.Note, error) {
	note, err := u.repo.GetNoteByID(id)
	if err != nil {
		return entity.Note{}, err
	}

	return note, nil
}

func (u Note) UpdateNote(note entity.Note) error {
	err := u.repo.UpdateNote(note)
	if err != nil {
		return err
	}

	return nil
}
