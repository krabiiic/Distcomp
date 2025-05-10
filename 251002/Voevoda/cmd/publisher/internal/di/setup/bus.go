package setup

import (
	"log/slog"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

func mustBus(cfg config.Config, logger *slog.Logger) *infra.Bus {
	bus := infra.NewBus(cfg.Kafka, logger)

	return bus
}
