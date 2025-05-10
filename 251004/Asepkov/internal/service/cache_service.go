package service

import (
	"RESTAPI/internal/storage"
	"context"
	"time"
)

type CacheService struct {
	redisClient *storage.RedisClient
}

func NewCacheService(redisClient *storage.RedisClient) *CacheService {
	return &CacheService{
		redisClient: redisClient,
	}
}

// Get retrieves a value from cache
func (cs *CacheService) Get(ctx context.Context, key string, dest interface{}) error {
	return cs.redisClient.Get(ctx, key, dest)
}

// Set stores a value in cache with expiration
func (cs *CacheService) Set(ctx context.Context, key string, value interface{}, expiration time.Duration) error {
	return cs.redisClient.Set(ctx, key, value, expiration)
}

// Delete removes a value from cache
func (cs *CacheService) Delete(ctx context.Context, key string) error {
	return cs.redisClient.Delete(ctx, key)
}

// GetOrSet retrieves a value from cache, or sets it if not found
func (cs *CacheService) GetOrSet(ctx context.Context, key string, dest interface{},
	setter func() (interface{}, error), expiration time.Duration) error {

	// Try to get from cache first
	err := cs.Get(ctx, key, dest)
	if err == nil {
		return nil
	}

	// If not in cache, get from setter
	value, err := setter()
	if err != nil {
		return err
	}

	// Store in cache
	err = cs.Set(ctx, key, value, expiration)
	if err != nil {
		return err
	}

	// Update dest with the new value
	return cs.Get(ctx, key, dest)
}

// InvalidateCache removes all cached items matching the pattern
func (cs *CacheService) InvalidateCache(ctx context.Context, pattern string) error {
	// Note: In a production environment, you might want to use SCAN instead of KEYS
	// for better performance with large datasets
	keys, err := cs.redisClient.Client.Keys(ctx, pattern).Result()
	if err != nil {
		return err
	}

	for _, key := range keys {
		if err := cs.Delete(ctx, key); err != nil {
			return err
		}
	}

	return nil
}
