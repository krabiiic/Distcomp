package inmemory

import (
	"errors"
	"sync/atomic"

	"github.com/strcarne/distributed-calculations/internal/entity"
	"github.com/strcarne/distributed-calculations/pkg/container"
)

var ErrNoteNotFound = errors.New("note not found")

type NoteRepository struct {
	storage *container.InMemoryStorage[int64, entity.Note]

	// counter is incremeted field that will be id of new notenr.
	counter atomic.Int64
}

func NewNoteRepository() *NoteRepository {
	return &NoteRepository{
		storage: container.DefaultInMemoryStorage[int64, entity.Note](),
		counter: atomic.Int64{},
	}
}

func (nr *NoteRepository) createNewID() int64 {
	return nr.counter.Add(1)
}

func (nr *NoteRepository) CreateNote(note entity.Note) (int64, error) {
	newID := nr.createNewID()
	note.ID = newID

	nr.storage.Set(newID, note)

	return newID, nil
}

func (nr *NoteRepository) DeleteNote(id int64) (entity.Note, error) {
	note, ok := nr.storage.GetWithOK(id)
	if !ok {
		return note, ErrNoteNotFound
	}

	nr.storage.Delete(id)

	return note, nil
}

func (nr *NoteRepository) GetAllNotes() ([]entity.Note, error) {
	return nr.storage.GetAll(), nil
}

func (nr *NoteRepository) GetNoteByID(id int64) (entity.Note, error) {
	note, ok := nr.storage.GetWithOK(id)
	if !ok {
		return note, ErrNoteNotFound
	}

	return note, nil
}

func (nr *NoteRepository) UpdateNote(note entity.Note) error {
	if !nr.storage.Has(note.ID) {
		return ErrNoteNotFound
	}

	nr.storage.Set(note.ID, note)

	return nil
}
