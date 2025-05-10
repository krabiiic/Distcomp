package com.example.publisher.repository.inMemory;

import com.example.publisher.model.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends BaseEntity> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(Long id);
    T update(T entity);
    void delete(Long id);
}
