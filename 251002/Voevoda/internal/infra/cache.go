package infra

import (
	"context"
	"encoding/json"
	"errors"
	"log"
	"time"

	"github.com/go-redis/redis/v8"
)

const CacheTTL = 1 * time.Minute

type Cache struct {
	client *redis.Client
}

func NewCache(cfg RedisConfig) Cache {
	log.Printf("Connecting to Redis at %s with password %s and db %d\n", cfg.Address, cfg.Password, cfg.DB)
	return Cache{
		client: redis.NewClient(&redis.Options{
			Addr:     cfg.Address,
			Password: cfg.Password,
			DB:       cfg.DB,
			Username: "default",
		}),
	}
}

func (c Cache) Get(ctx context.Context, key string) ([]byte, bool, error) {
	val, err := c.client.Get(ctx, key).Bytes()
	if err != nil {
		if errors.Is(err, redis.Nil) {
			return nil, false, nil
		}

		return nil, false, err
	}

	return val, true, nil
}

func (c Cache) Set(ctx context.Context, key string, value []byte) error {
	return c.client.Set(ctx, key, value, CacheTTL).Err()
}

func (c Cache) Delete(ctx context.Context, key string) error {
	return c.client.Del(ctx, key).Err()
}

func CacheSet[T any](c Cache, key string, value T) error {
	json, err := json.Marshal(value)
	if err != nil {
		return err
	}

	return c.Set(context.Background(), key, json)
}

func CacheGet[T any](c Cache, key string) (T, bool, error) {
	val, found, err := c.Get(context.Background(), key)
	if err != nil {
		var zero T
		return zero, false, err
	}

	var result T
	if err := json.Unmarshal(val, &result); err != nil {
		var zero T
		return zero, false, err
	}

	return result, found, nil
}
