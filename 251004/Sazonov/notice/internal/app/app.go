package app

import (
	"context"
	"fmt"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/config"
	grpchandler "github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/grpc"
	httphandler "github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/http"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/kafka"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/repository"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/service"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/cassandra"
	grpcserver "github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/grpc"
	httpserver "github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/http"
	kafkaserver "github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/kafka"
	appbase "github.com/Khmelov/Distcomp/251004/Sazonov/pkg/app"
)

type app struct {
	*appbase.App
}

func New(cfg config.Config) (*app, error) {
	repo, err := repository.New(
		cassandra.Config{
			Addrs:    cfg.Casandra.Addrs,
			User:     cfg.Casandra.User,
			Password: cfg.Casandra.Password,
			Keyspace: cfg.Casandra.Keyspace,
		},
	)
	if err != nil {
		return nil, fmt.Errorf("new app: %w", err)
	}

	svc := service.New(repo)

	httpServer := httpserver.NewServer(
		httpserver.Config{
			Host:        cfg.HTTP.Host,
			Port:        cfg.HTTP.Port,
			Timeout:     cfg.HTTP.Timeout,
			IdleTimeout: cfg.HTTP.IdleTimeout,
		}, httphandler.New(svc))

	grpcServer := grpcserver.NewServer(
		grpcserver.Config{
			Host:    cfg.GRPC.Host,
			Port:    cfg.GRPC.Port,
			Timeout: cfg.GRPC.Timeout,
		},
	)

	grpchandler.New(svc).Register(grpcServer)

	consumer := kafkaserver.NewConsumer(
		kafkaserver.ConsumerConfig{
			Brokers: cfg.Kafka.Brokers,
			GroupID: cfg.Kafka.GroupID,
			Topic:   cfg.Kafka.Topic,
		},
	)
	kafkaServer := kafkaserver.NewServer(
		consumer,
		kafka.New(svc),
	)

	app := &app{
		App: appbase.New(
			[]appbase.Service{httpServer, grpcServer, kafkaServer},
			[]appbase.CleanupFunc{
				repo.Close,
				func() {
					consumer.Close()
				},
			},
		),
	}

	return app, nil
}

func (a *app) Shutdown(ctx context.Context) error {
	return a.App.Shutdown(ctx)
}
