package setup

import (
	"context"
	"log"
	"log/slog"

	"github.com/jackc/pgx/v5"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
)

func mustDB(cfg config.Config) *pgx.Conn {
	dsn := cfg.Postgres.DatabaseURL
	slog.Info("connecting to database", slog.String("dsn", dsn))

	db, err := pgx.Connect(context.Background(), dsn)
	if err != nil {
		log.Panicf("failed to connect to database: %s", err)
	}

	return db
}
