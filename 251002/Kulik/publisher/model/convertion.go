package model

import (
	"github.com/jinzhu/copier"
	"time"
)

func CreatorToModel(dto CreatorRequestTo) Creator {
	var creator Creator
	copier.Copy(&creator, &dto)
	return creator
}

func CreatorToDTO(creator Creator) CreatorResponseTo {
	var dto CreatorResponseTo
	copier.Copy(&dto, &creator)
	return dto
}

func StoryToModel(dto StoryRequestTo) Story {
	var story Story
	copier.Copy(&story, &dto)
	story.Created = time.Now()
	story.Modified = time.Now()
	return story
}

func StoryToDTO(story Story) StoryResponseTo {
	var dto StoryResponseTo
	copier.Copy(&dto, &story)
	return dto
}

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

func MarkToModel(dto MarkRequestTo) Mark {
	var mark Mark
	copier.Copy(&mark, &dto)
	return mark
}

func MarkToDTO(mark Mark) MarkResponseTo {
	var dto MarkResponseTo
	copier.Copy(&dto, &mark)
	return dto
}
