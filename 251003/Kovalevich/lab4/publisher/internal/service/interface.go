package service

import (
	"context"

	m "lab3/publisher/internal/model"
)

type WriterService interface {
	CreateWriter(ctx context.Context, cr m.Writer) (m.Writer, error)
	GetWriters(ctx context.Context) ([]m.Writer, error)
	GetWriterByID(ctx context.Context, id int) (m.Writer, error)
	UpdateWriterByID(ctx context.Context, cr m.Writer) (m.Writer, error)
	DeleteWriterByID(ctx context.Context, id int) error
}

type TopicService interface {
	CreateTopic(ctx context.Context, Topic m.Topic) (m.Topic, error)
	GetTopics(ctx context.Context) ([]m.Topic, error)
	GetTopicByID(ctx context.Context, id int64) (m.Topic, error)
	UpdateTopicByID(ctx context.Context, Topic m.Topic) (m.Topic, error)
	DeleteTopicByID(ctx context.Context, id int64) error
}

type PostService interface {
	CreatePost(ctx context.Context, Post m.Post) (m.Post, error)
	GetPosts(ctx context.Context) ([]m.Post, error)
	GetPostByID(ctx context.Context, id int64) (m.Post, error)
	UpdatePostByID(ctx context.Context, Post m.Post) (m.Post, error)
	DeletePostByID(ctx context.Context, id int64) error
}

type MarkService interface {
	CreateMark(ctx context.Context, mark m.Mark) (m.Mark, error)
	GetMarks(ctx context.Context) ([]m.Mark, error)
	GetMarkByID(ctx context.Context, id int64) (m.Mark, error)
	UpdateMarkByID(ctx context.Context, mark m.Mark) (m.Mark, error)
	DeleteMarkByID(ctx context.Context, id int64) error
}
