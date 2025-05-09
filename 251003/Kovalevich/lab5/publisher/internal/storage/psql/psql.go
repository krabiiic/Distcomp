package psql

import (
	Topic "lab3/publisher/internal/storage/psql/topic"
	"lab3/publisher/internal/storage/psql/writer"
	"log"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"lab3/publisher/internal/storage/psql/mark"
)

type PSQL struct {
	db *sqlx.DB

	WriterInst Writer.Writer
	TopicInst  Topic.Topic
	MarkInst   mark.Mark
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

		WriterInst: Writer.New(db),
		TopicInst:  Topic.New(db),
		MarkInst:   mark.New(db),
	}, nil
}

func (p *PSQL) Close() {
	if err := p.db.Close(); err != nil {
		log.Println("Error closing DB:", err)
	}
}
