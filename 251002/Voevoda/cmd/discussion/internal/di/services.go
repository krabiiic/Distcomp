package di

import (
	"github.com/strcarne/distributed-calculations/internal/entity"
)

type Services struct {
	Note NoteService
}

type NoteService interface {
	CreateNote(note entity.Note) (entity.Note, error)
	GetNoteByID(id int64) (entity.Note, error)
	GetAllNotes() ([]entity.Note, error)
	UpdateNote(note entity.Note) error
	DeleteNote(id int64) (entity.Note, error)
}
