package storage

import (
	"lab2/internal/storage/post"
	"lab2/internal/storage/writer"

	"lab2/internal/storage/mark"

	"lab2/internal/storage/topic"

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
