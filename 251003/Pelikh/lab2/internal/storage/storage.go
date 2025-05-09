package storage

import (
	"lab2/internal/storage/creator"
	"lab2/internal/storage/marker"
	"lab2/internal/storage/notice"
	"lab2/internal/storage/topic"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
)

type Storage struct {
	db *sqlx.DB

	Creator creator.Repo
	Topic   topic.Repo
	Notice  notice.Repo
	Marker  marker.Repo
}

func New() (Storage, error) {
	cfg := NewConfig()

	db, err := sqlx.Connect("postgres", cfg.DSN())
	if err != nil {
		return Storage{}, err
	}

	return Storage{
		db: db,

		Creator: creator.New(db),
		Topic:   topic.New(db),
		Notice:  notice.New(db),
		Marker:  marker.New(db),
	}, nil
}

func (p Storage) Close() {
	p.db.Close()
}
