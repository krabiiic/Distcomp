//nolint:ireturn
package container

import "sync"

const InMemoryStorageDefaultCapacity = 16

type InMemoryStorage[K comparable, V any] struct {
	storage map[K]V
	mx      sync.RWMutex
}

type Pair[K comparable, V any] struct {
	Key   K
	Value V
}

func NewInMemoryStorage[K comparable, V any](initialCapacity int) *InMemoryStorage[K, V] {
	return &InMemoryStorage[K, V]{
		storage: make(map[K]V, initialCapacity),
		mx:      sync.RWMutex{},
	}
}

func DefaultInMemoryStorage[K comparable, V any]() *InMemoryStorage[K, V] {
	return NewInMemoryStorage[K, V](InMemoryStorageDefaultCapacity)
}

func (s *InMemoryStorage[K, V]) Get(key K) V {
	s.mx.RLock()
	defer s.mx.RUnlock()

	return s.storage[key]
}

func (s *InMemoryStorage[K, V]) GetWithOK(key K) (V, bool) {
	s.mx.RLock()
	defer s.mx.RUnlock()

	v, ok := s.storage[key]

	return v, ok
}

func (s *InMemoryStorage[K, V]) GetAll() []V {
	s.mx.RLock()
	defer s.mx.RUnlock()

	all := make([]V, 0, len(s.storage))
	for _, v := range s.storage {
		all = append(all, v)
	}

	return all
}

func (s *InMemoryStorage[K, V]) Has(key K) bool {
	s.mx.RLock()
	defer s.mx.RUnlock()

	_, ok := s.storage[key]

	return ok
}

func (s *InMemoryStorage[K, V]) Set(key K, value V) {
	s.mx.Lock()
	defer s.mx.Unlock()

	s.storage[key] = value
}

func (s *InMemoryStorage[K, V]) SetBatch(batch []Pair[K, V]) {
	s.mx.Lock()
	defer s.mx.Unlock()

	for i := range batch {
		s.storage[batch[i].Key] = batch[i].Value
	}
}

func (s *InMemoryStorage[K, V]) Delete(key K) {
	s.mx.Lock()
	defer s.mx.Unlock()

	delete(s.storage, key)
}
