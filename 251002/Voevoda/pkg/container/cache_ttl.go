package container

import (
	"bytes"
	"context"
	"encoding/gob"
	"hash/maphash"
	"runtime"
	"time"
	"unsafe"
)

type CacheTTL[K comparable, V any] struct {
	shards []*shardTTL[K, V]
}

func NewCacheTTL[K comparable, V any]() CacheTTL[K, V] {
	shards := make([]*shardTTL[K, V], runtime.NumCPU())
	for i := 0; i < runtime.NumCPU(); i++ {
		shards[i] = newShardTTL[K, V]()
	}

	cache := CacheTTL[K, V]{
		shards: shards,
	}

	return cache
}

func (c *CacheTTL[K, V]) Set(key K, value V, ttl time.Duration) {
	shard := c.getShard(key)
	shard.Set(key, value, ttl)
}

func (c *CacheTTL[K, V]) Get(key K) (V, bool) {
	shard := c.getShard(key)
	return shard.Get(key)
}

func (c *CacheTTL[K, V]) Delete(key K) {
	shard := c.getShard(key)
	shard.Delete(key)
}

func (c *CacheTTL[K, V]) Cleanup() {
	for _, shard := range c.shards {
		shard.Cleanup()
	}
}

func (c *CacheTTL[K, V]) StartCleanUpByInterval(ctx context.Context, interval time.Duration) {
	ticker := time.NewTicker(interval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			c.Cleanup()
		case <-ctx.Done():
			return
		}
	}
}

func (c CacheTTL[K, V]) getShard(key K) *shardTTL[K, V] {
	return c.shards[c.getHash(key)%uint64(len(c.shards))]
}

func (c CacheTTL[K, V]) getHash(key K) uint64 {
	var h maphash.Hash

	switch k := any(key).(type) {
	case string:
		h.WriteString(k)
		return h.Sum64()
	case int:
		b := (*[8]byte)(unsafe.Pointer(&k))[:8]
		h.Write(b)
		return h.Sum64()
	case int64:
		b := (*[8]byte)(unsafe.Pointer(&k))[:8]
		h.Write(b)
		return h.Sum64()
	case int32:
		b := (*[4]byte)(unsafe.Pointer(&k))[:4]
		h.Write(b)
		return h.Sum64()
	case int16:
		b := (*[2]byte)(unsafe.Pointer(&k))[:2]
		h.Write(b)
		return h.Sum64()
	case int8:
		b := (*[1]byte)(unsafe.Pointer(&k))[:1]
		h.Write(b)
		return h.Sum64()
	case uint:
		b := (*[8]byte)(unsafe.Pointer(&k))[:8]
		h.Write(b)
		return h.Sum64()
	case uint64:
		b := (*[8]byte)(unsafe.Pointer(&k))[:8]
		h.Write(b)
		return h.Sum64()
	case uint32:
		b := (*[4]byte)(unsafe.Pointer(&k))[:4]
		h.Write(b)
		return h.Sum64()
	case uint16:
		b := (*[2]byte)(unsafe.Pointer(&k))[:2]
		h.Write(b)
		return h.Sum64()
	case uint8:
		b := (*[1]byte)(unsafe.Pointer(&k))[:1]
		h.Write(b)
		return h.Sum64()
	case float64:
		b := (*[8]byte)(unsafe.Pointer(&k))[:8]
		h.Write(b)
		return h.Sum64()
	case float32:
		b := (*[4]byte)(unsafe.Pointer(&k))[:4]
		h.Write(b)
		return h.Sum64()
	case bool:
		var b [1]byte
		if k {
			b[0] = 1
		}
		h.Write(b[:])
		return h.Sum64()
	default:
		var buf bytes.Buffer
		enc := gob.NewEncoder(&buf)
		enc.Encode(key)
		h.Write(buf.Bytes())
		return h.Sum64()
	}
}
