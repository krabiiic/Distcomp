package com.example.publisher.repository.inMemory;

import com.example.publisher.model.BaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<T extends BaseEntity> implements Repository<T> {
    private final Map<Long, T> storage = new HashMap<>();
    private long idCounter = 1;

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
        }
        storage.put(entity.getId(), entity);
        return storage.get(entity.getId());
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public T update(T entity) {
        if (entity.getId() != null && storage.containsKey(entity.getId())) {
            storage.put(entity.getId(), entity);
            return storage.get(entity.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(Long id) {
        if (storage.get(id) != null) {
            storage.remove(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

