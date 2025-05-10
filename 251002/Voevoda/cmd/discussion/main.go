package main

import (
	"context"
	"flag"
	"log/slog"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/config"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/di/setup"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/handler/httpapi"
	"github.com/strcarne/distributed-calculations/cmd/discussion/internal/handler/kafkaapi"
)

func main() {
	configPath := flag.String("config", "configs", "path to the configs directory or config file")
	flag.Parse()

	cfg := config.MustParseConfig(*configPath)
	deps := setup.MustContainer(cfg)

	kafkaHandler := kafkaapi.NewHandler(deps)

	go func() {
		if err := kafkaHandler.Start(context.Background()); err != nil {
			deps.Logger.Error("failed to start kafka handler", slog.String("error", err.Error()))
		}
	}()

	router := chi.NewRouter()
	httpapi.RegisterV1(router, deps)

	deps.Logger.Info("starting server", slog.String("address", cfg.Server.Address))
	if err := http.ListenAndServe(cfg.Server.Address, router); err != nil {
		deps.Logger.Error("failed to start listening", slog.String("address", cfg.Server.Address))
	}
}
