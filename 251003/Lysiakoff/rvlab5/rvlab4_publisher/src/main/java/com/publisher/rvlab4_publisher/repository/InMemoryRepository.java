package com.publisher.rvlab4_publisher.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRepository<T> {
    private final Map<Long, T> storage = new ConcurrentHashMap<>();

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public T save(Long id, T entity) {
        storage.put(id, entity);
        return entity;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}




