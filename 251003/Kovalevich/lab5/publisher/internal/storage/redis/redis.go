package redis

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis/v8"
	"time"

	"lab3/publisher/internal/model"
)

type Cache struct {
	DB *redis.Client
}

func NewCache() *Cache {
	redisClient := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})

	return &Cache{
		DB: redisClient,
	}
}

func (c *Cache) SaveMessage(ctx context.Context, msg *model.Post, ttl time.Duration) error {
	data, err := json.Marshal(msg)
	if err != nil {
		return err
	}

	return c.DB.Set(ctx, c.messageKey(msg.ID), data, ttl).Err()
}

func (c *Cache) GetMessage(ctx context.Context, id int) (*model.Post, error) {
	data, err := c.DB.Get(ctx, c.messageKey(id)).Bytes()
	if err != nil {
		if err == redis.Nil {
			return nil, nil
		}
		return nil, err
	}

	var msg model.Post
	if err := json.Unmarshal(data, &msg); err != nil {
		return nil, err
	}

	return &msg, nil
}

func (c *Cache) GetAllMessages(ctx context.Context) ([]model.Post, error) {
	keys, err := c.DB.Keys(ctx, "message:*").Result()
	if err != nil {
		return nil, err
	}

	var messages []model.Post
	for _, key := range keys {
		data, err := c.DB.Get(ctx, key).Bytes()
		if err != nil {
			continue
		}

		var msg model.Post
		if err := json.Unmarshal(data, &msg); err == nil {
			messages = append(messages, msg)
		}
	}

	return messages, nil
}

func (c *Cache) DeleteMessage(ctx context.Context, id int) error {
	return c.DB.Del(ctx, c.messageKey(id)).Err()
}

func (c *Cache) messageKey(id int) string {
	return fmt.Sprintf("message:%d", id)
}
