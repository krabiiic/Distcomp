package service

import (
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/service/mark"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/service/post"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/service/topic"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/service/writer"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage"
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
