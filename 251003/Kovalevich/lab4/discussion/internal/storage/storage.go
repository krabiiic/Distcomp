package storage

import (
	"fmt"
	Post "lab3/discussion/internal/storage/cassandra/post"

	"github.com/gocql/gocql"
	"lab3/discussion/pkg/cassandra"
)

type repository struct {
	session *gocql.Session

	Post.PostRepo
}

func New(cfg cassandra.Config) (Repository, error) {
	session, err := cassandra.Connect(cfg)
	if err != nil {
		return nil, fmt.Errorf("new repository: %w", err)
	}

	repo := &repository{
		session: session,

		PostRepo: *Post.New(session),
	}

	return repo, nil
}

func (r repository) Close() {
	r.session.Close()
}
