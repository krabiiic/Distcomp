package setup

import (
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di"
)

func MustContainer(cfg config.Config) di.Container {
	db := mustDB(cfg)
	queries := mustQueries(db)
	services := mustServices(queries)

	logger := mustLogger(cfg)
	bus := mustBus(cfg, logger)

	return di.Container{
		Config:   cfg,
		Bus:      bus,
		Queries:  queries,
		Services: services,
		Logger:   logger,
		Cache:    mustCache(cfg),
	}
}
