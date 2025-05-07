package main

import (
	"context"
	"flag"
	"log/slog"
	"net/http"

	"github.com/go-chi/chi/v5"

	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/config"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/di/setup"
	"github.com/strcarne/distributed-calculations/cmd/publisher/internal/handler/httpapi"
)

func main() {
	configPath := flag.String("config", "configs", "path to the configs directory or config file")
	flag.Parse()

	cfg := config.MustParseConfig(*configPath)
	deps := setup.MustContainer(cfg)

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	go deps.Bus.StartBackgroundConsumer(ctx)

	router := chi.NewRouter()
	httpapi.RegisterV1(router, deps)

	deps.Logger.Info("starting server", slog.String("address", cfg.Server.Address))
	if err := http.ListenAndServe(cfg.Server.Address, router); err != nil {
		deps.Logger.Error("failed to start listening", slog.String("address", cfg.Server.Address))
	}
}
