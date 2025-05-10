package storage

import (
	"lab3/publisher/internal/storage/psql"
	"lab3/publisher/internal/storage/redis"
)

type Cache struct {
	DB *redis.Cache
}

type Storage struct {
	DB *psql.PSQL
}

func New() (*Storage, error) {
	db, err := psql.New()
	if err != nil {
		return nil, err
	}

	return &Storage{
		DB: db,
	}, nil
}

func NewCache() *Cache {
	return &Cache{
		DB: redis.NewCache(),
	}
}
