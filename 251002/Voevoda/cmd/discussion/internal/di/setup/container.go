package setup

import (
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/config"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/di"
)

func MustContainer(cfg config.Config) di.Container {
	session := mustSession(cfg)

	logger := mustLogger(cfg)
	bus := mustBus(cfg, logger)

	container := di.Container{
		Config:   cfg,
		Bus:      bus,
		Session:  session,
		Logger:   logger,
		Services: mustServices(session, logger),
	}

	return container
}
