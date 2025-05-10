package service

import (
	"lab3/publisher/internal/api/discussion"
	"lab3/publisher/internal/service/mark"
	Post "lab3/publisher/internal/service/post"
	Topic "lab3/publisher/internal/service/topic"
	Writer "lab3/publisher/internal/service/writer"
	"lab3/publisher/internal/storage"
)

type Service struct {
	db storage.Storage

	Writer WriterService
	Topic  TopicService
	Mark   MarkService
	Post   PostService
}

func New(db storage.Storage) Service {
	return Service{
		db: db,

		Writer: Writer.New(db.DB.WriterInst),
		Topic:  Topic.New(db.DB.TopicInst),
		Mark:   mark.New(db.DB.MarkInst),
		Post:   Post.New(discussion.NewClient(), *storage.NewCache()),
	}
}
