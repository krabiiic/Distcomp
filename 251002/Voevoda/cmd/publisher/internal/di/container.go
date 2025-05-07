package di

import (
	"log/slog"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/repository/psql/generated"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

type Container struct {
	Config   config.Config
	Bus      *infra.Bus
	Cache    infra.Cache
	Queries  *generated.Queries
	Logger   *slog.Logger
	Services Services
}
