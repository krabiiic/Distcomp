package psql

import (
	//"lab3/publisher/internal/handler/http/creator"
	//"lab3/publisher/internal/handler/http/topic"
	"log"

	"lab3/publisher/internal/storage/psql/creator"
	"lab3/publisher/internal/storage/psql/marker"
	"lab3/publisher/internal/storage/psql/topic"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
)

type PSQL struct {
	db *sqlx.DB

	CreatorInst creator.Creator ///////////////////////////////////////////////////
	TopicInst   topic.Topic
	MarkerInst  marker.Marker
}

func New() (*PSQL, error) {
	cfg := NewConfig()

	db, err := sqlx.Connect("postgres", cfg.DSN())
	if err != nil {
		return nil, err
	}

	log.Println("Connected to PostgreSQL")

	return &PSQL{
		db: db,

		CreatorInst: creator.New(db), /////////////////////////////////////////////////////////
		TopicInst:   topic.New(db),
		MarkerInst:  marker.New(db),
	}, nil
}

func (p *PSQL) Close() {
	if err := p.db.Close(); err != nil {
		log.Println("Error closing DB:", err)
	}
}
