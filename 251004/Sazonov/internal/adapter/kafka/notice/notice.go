package notice

import (
	"context"
	"encoding/json"

	"github.com/Khmelov/Distcomp/251004/Sazonov/internal/model"
	"github.com/google/uuid"
	"github.com/segmentio/kafka-go"
)

func (a *Adapter) CreateNoticeAsync(ctx context.Context, args model.Notice) error {
	value, err := json.Marshal(args)
	if err != nil {
		return err
	}

	err = a.producer.WriteMessages(ctx, kafka.Message{
		Key:   []byte(uuid.New().String()),
		Value: value,
	})
	if err != nil {
		return err
	}

	return nil
}
