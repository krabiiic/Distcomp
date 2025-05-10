package kafka

import "github.com/segmentio/kafka-go"

type ProducerConfig struct {
	Brokers []string
	Topic   string
}

type Producer struct {
	*kafka.Writer
}

func NewProducer(cfg ProducerConfig) *Producer {
	w := kafka.NewWriter(kafka.WriterConfig{
		Brokers:  cfg.Brokers,
		Topic:    cfg.Topic,
		Balancer: &kafka.RoundRobin{},
	})
	w.AllowAutoTopicCreation = true

	producer := &Producer{Writer: w}

	return producer
}
