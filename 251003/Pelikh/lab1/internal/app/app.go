package app

import (
	"context"
	"log"

	"lab1/internal/handler"
	"lab1/internal/server"
	"lab1/internal/service"
	"lab1/internal/storage"
)

type App struct {
	srv server.Server
}

func New() (App, error) {
	db, err := storage.New()
	if err != nil {
		return App{}, err
	}

	svc := service.New(db)

	srv := server.New(
		handler.New(svc),
		server.NewDefaultConfig(),
	)

	app := App{
		srv: srv,
	}

	return app, nil
}

func (a App) Start(ctx context.Context) error {
	log.Println("app started")

	if err := a.srv.Serve(ctx); err != nil {
		log.Printf("app stopped with error: %v\n", err)

		return err
	}

	log.Println("app stopped normally")

	return nil
}
