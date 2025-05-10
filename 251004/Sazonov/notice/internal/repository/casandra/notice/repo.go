package notice

import (
	"sync/atomic"

	"github.com/gocql/gocql"
)

type NoticeRepo struct {
	session *gocql.Session

	id atomic.Int64
}

func New(session *gocql.Session) *NoticeRepo {
	return &NoticeRepo{
		session: session,
	}
}

func (n *NoticeRepo) nextID() int64 {
	return n.id.Add(1)
}
