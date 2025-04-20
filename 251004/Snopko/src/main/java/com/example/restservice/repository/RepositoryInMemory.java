package com.example.restservice.repository;

import com.example.restservice.model.Identifiable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RepositoryInMemory <T extends Identifiable> {
    private final Map<Long, T> map = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    public T save(T data) {
        if (data.getId() == null) {
            data.setId(idCounter.getAndIncrement());
        }
        map.put(data.getId(), data);
        return data;
    }

    public T update(T data) {
        if (!map.containsKey(data.getId())) {
            throw new IllegalArgumentException("Object with id " + data.getId() + " not found");
        }
        map.put(data.getId(), data);
        return data;
    }

    public void deleteById(Long id) {
        map.remove(id);
    }
}
