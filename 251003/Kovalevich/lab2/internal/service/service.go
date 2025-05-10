package service

import (
	"lab2/internal/service/post"
	"lab2/internal/service/writer"

	"lab2/internal/service/mark"

	"lab2/internal/service/topic"

	"lab2/internal/storage"
)

type Service struct {
	Writer writer.Service
	Topic  topic.Service
	Post   post.Service
	Mark   mark.Service
}

func New(db storage.Storage) Service {

	return Service{
		Writer: writer.New(db.Writer),
		Topic:  topic.New(db.Topic),
		Post:   post.New(db.Post),
		Mark:   mark.New(db.Mark),
	}
}
