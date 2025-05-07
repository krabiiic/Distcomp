package kafka

import (
	"context"
	"time"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/logger"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

const serverConsumeTimeout = 200 * time.Millisecond

type Handler interface {
	Handle(ctx context.Context, m kafka.Message) error
}

type server struct {
	consumer *Consumer
	handler  Handler

	quit chan struct{}
}

func NewServer(consumer *Consumer, handler Handler) *server {
	return &server{
		consumer: consumer,
		handler:  handler,
		quit:     make(chan struct{}),
	}
}

func (s *server) Run(ctx context.Context) error {
	timer := time.NewTimer(serverConsumeTimeout)
	defer timer.Stop()

	for {
		timer.Reset(serverConsumeTimeout)

		select {
		case <-s.quit:
			return nil

		case <-timer.C:
			m, err := s.consumer.ReadMessage(ctx)
			if err != nil {
				logger.Error(ctx, "run kafka server: read message", zap.Error(err))
				continue
			}

			if err := s.handler.Handle(ctx, m); err != nil {
				logger.Error(ctx, "run kafka server: handle message", zap.Error(err))
				continue
			}
		}
	}
}

func (s *server) Shutdown(ctx context.Context) error {
	s.quit <- struct{}{}
	return nil
}
