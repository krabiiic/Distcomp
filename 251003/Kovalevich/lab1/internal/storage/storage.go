package storage

import (
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage/mark"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage/post"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage/topic"
	"github.com/Khmelov/Distcomp/251003/truhan/lab1/internal/storage/writer"
	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
)

type Storage struct {
	db *sqlx.DB

	Writer writer.Repo
	Topic  topic.Repo
	Post   post.Repo
	Mark   mark.Repo
}

func New() (Storage, error) {
	cfg := NewConfig()

	db, err := sqlx.Connect("postgres", cfg.DSN())
	if err != nil {
		return Storage{}, err
	}

	return Storage{
		db: db,

		Writer: writer.New(db),
		Topic:  topic.New(db),
		Post:   post.New(db),
		Mark:   mark.New(db),
	}, nil
}

func (p Storage) Close() {
	p.db.Close()
}
