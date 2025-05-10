package setup

import (
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
	"github.com/strcarne/distributed-calculations/internal/infra"
)

func mustCache(cfg config.Config) infra.Cache {
	return infra.NewCache(cfg.Redis)
}
