package kafka

import (
	"context"

	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/handler/kafka/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/notice/internal/service"
	kafkapkg "github.com/Khmelov/Distcomp/251004/Sazonov/notice/pkg/kafka"
	"github.com/segmentio/kafka-go"
)

type handler struct {
	notice *notice.NoticeHandler
}

func New(service service.Service) kafkapkg.Handler {
	return handler{
		notice: notice.New(service),
	}
}

func (h handler) Handle(ctx context.Context, m kafka.Message) error {
	return h.notice.Handle(ctx, m)
}
