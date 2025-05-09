package app

import (
	"context"
	"fmt"
	"log"
	"os/signal"
	"syscall"

	"lab3/discussion/internal/server"
	"lab3/discussion/internal/service"
)

type app struct {
}

func New() *app {
	return &app{}
}

func (a app) Start(ctx context.Context, service service.PostService) error {
	ctx, stop := signal.NotifyContext(ctx, syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	srv := server.New(service)

	if err := srv.Serve(ctx); err != nil {
		return fmt.Errorf("server stopped with error: %v", err)
	}

	log.Println("server stopped")

	return nil
}
