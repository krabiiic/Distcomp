package di

import (
	"log/slog"

	"github.com/gocql/gocql"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/config"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

type Container struct {
	Config   config.Config
	Bus      *infra.Bus
	Session  *gocql.Session
	Logger   *slog.Logger
	Services Services
}
