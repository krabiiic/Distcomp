package container

import (
	"sync"
	"time"
)

const (
	defaultShardSize = 128
)

type shardTTL[K comparable, V any] struct {
	m  map[K]cacheItem[K, V]
	mu sync.RWMutex
}

type cacheItem[K comparable, V any] struct {
	Key       K
	Value     V
	ExpiredAt time.Time
}

func newShardTTL[K comparable, V any]() *shardTTL[K, V] {
	return &shardTTL[K, V]{m: make(map[K]cacheItem[K, V], defaultShardSize)}
}

// Set adds or updates a key-value pair with a TTL
func (s *shardTTL[K, V]) Set(key K, value V, ttl time.Duration) {
	s.mu.Lock()
	defer s.mu.Unlock()

	expiredAt := time.Now().Add(ttl)
	s.m[key] = cacheItem[K, V]{
		Key:       key,
		Value:     value,
		ExpiredAt: expiredAt,
	}
}

// Get retrieves a value by key if it exists and hasn't expired
func (s *shardTTL[K, V]) Get(key K) (V, bool) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	item, exists := s.m[key]
	if !exists {
		var zero V
		return zero, false
	}

	// Check if item has expired
	if time.Now().After(item.ExpiredAt) {
		delete(s.m, key)
		var zero V
		return zero, false
	}

	return item.Value, true
}

// Delete removes an item from the cache
func (s *shardTTL[K, V]) Delete(key K) {
	s.mu.Lock()
	defer s.mu.Unlock()

	delete(s.m, key)
}

// Clear removes all items from the cache
func (s *shardTTL[K, V]) Clear() {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Replace with a new map instead of clearing each item
	s.m = make(map[K]cacheItem[K, V], defaultShardSize)
}

// Size returns the number of items in the shard
func (s *shardTTL[K, V]) Size() int {
	s.mu.Lock()
	defer s.mu.Unlock()

	return len(s.m)
}

// Cleanup removes all expired items from the cache
func (s *shardTTL[K, V]) Cleanup() int {
	s.mu.Lock()
	defer s.mu.Unlock()

	now := time.Now()
	removed := 0

	for key, item := range s.m {
		if now.After(item.ExpiredAt) {
			delete(s.m, key)
			removed++
		}
	}

	return removed
}
