package model

import (
	"github.com/jinzhu/copier"
)

func NoteToModel(dto NoteRequestTo) Note {
	var note Note
	copier.Copy(&note, &dto)
	return note
}

func NoteToDTO(note Note) NoteResponseTo {
	var dto NoteResponseTo
	copier.Copy(&dto, &note)
	return dto
}
