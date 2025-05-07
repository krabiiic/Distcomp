package repository

import (
	"fmt"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/repository/casandra/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/cassandra"
	"github.com/gocql/gocql"
)

type repository struct {
	session *gocql.Session

	NoticeRepo
}

func New(cfg cassandra.Config) (Repository, error) {
	session, err := cassandra.Connect(cfg)
	if err != nil {
		return nil, fmt.Errorf("new repository: %w", err)
	}

	repo := &repository{
		session: session,

		NoticeRepo: notice.New(session),
	}

	return repo, nil
}

func (r repository) Close() {
	r.session.Close()
}
