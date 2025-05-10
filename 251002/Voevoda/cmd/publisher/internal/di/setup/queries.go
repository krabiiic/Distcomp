package setup

import (
	"log"

	"github.com/jackc/pgx/v5"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
)

func mustQueries(db *pgx.Conn) *generated.Queries {
	queries := generated.New(db)
	if queries == nil {
		log.Panicf("failed to create sqlc queries")
	}

	return queries
}
