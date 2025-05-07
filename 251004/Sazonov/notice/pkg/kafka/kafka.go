package kafka

import (
	"github.com/segmentio/kafka-go"
)

const (
	consumerMaxBytes int = 1e6
)

type ConsumerConfig struct {
	Brokers []string
	GroupID string
	Topic   string
}

type Consumer struct {
	*kafka.Reader
}

func NewConsumer(cfg ConsumerConfig) *Consumer {
	r := kafka.NewReader(kafka.ReaderConfig{
		Brokers: cfg.Brokers,
		Topic:   cfg.Topic,
		GroupID: cfg.GroupID,

		StartOffset: kafka.LastOffset,
		MaxBytes:    consumerMaxBytes,
	})

	consumer := &Consumer{Reader: r}

	return consumer
}
