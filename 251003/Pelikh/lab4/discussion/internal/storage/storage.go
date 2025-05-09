package storage

import (
	"fmt"
	"lab3/discussion/internal/storage/cassandra/notice"

	"github.com/gocql/gocql"
	"lab3/discussion/pkg/cassandra"
)

type repository struct {
	session *gocql.Session

	notice.NoticeRepo
}

func New(cfg cassandra.Config) (Repository, error) {
	session, err := cassandra.Connect(cfg)
	if err != nil {
		return nil, fmt.Errorf("new repository: %w", err)
	}

	repo := &repository{
		session: session,

		NoticeRepo: *notice.New(session),
	}

	return repo, nil
}

func (r repository) Close() {
	r.session.Close()
}
