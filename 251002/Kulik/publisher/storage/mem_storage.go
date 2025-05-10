package storage

import (
	"errors"
	"sync"
)

type MemStorage[T any] struct {
	data   map[int64]T
	mutex  sync.RWMutex
	nextID int64
}

func NewInMemStorage[T any]() *MemStorage[T] {
	return &MemStorage[T]{data: make(map[int64]T), nextID: 1}
}

func (repo *MemStorage[T]) Create(entity T) (int64, error) {
	repo.mutex.Lock()
	defer repo.mutex.Unlock()

	id := repo.nextID
	repo.nextID++
	repo.data[id] = entity

	return id, nil
}

func (repo *MemStorage[T]) Get(id int64) (T, error) {
	repo.mutex.RLock()
	defer repo.mutex.RUnlock()

	entity, exists := repo.data[id]
	if !exists {
		var zero T
		return zero, errors.New("not found")
	}
	return entity, nil
}

func (repo *MemStorage[T]) Update(id int64, entity T) error {
	repo.mutex.Lock()
	defer repo.mutex.Unlock()

	if _, exists := repo.data[id]; !exists {
		return errors.New("not found")
	}
	repo.data[id] = entity
	return nil
}

func (repo *MemStorage[T]) Delete(id int64) error {
	repo.mutex.Lock()
	defer repo.mutex.Unlock()

	if _, exists := repo.data[id]; !exists {
		return errors.New("not found")
	}
	delete(repo.data, id)
	return nil
}

func (repo *MemStorage[T]) GetAll() ([]T, []int64, error) {
	repo.mutex.RLock()
	defer repo.mutex.RUnlock()

	result := make([]T, 0, len(repo.data))
	ids := make([]int64, 0, len(repo.data))
	for id, entity := range repo.data {
		ids = append(ids, id)
		result = append(result, entity)
	}

	return result, ids, nil
}