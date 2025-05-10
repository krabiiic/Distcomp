package com.example.JPA2.Impl.Services;

import jakarta.validation.constraints.Min;

import java.util.List;

public interface Service<T, K> {
    List<T> getAll();
    T update(K requestTo);
    T get(@Min(0) long id);
    T delete(@Min(0) long id);
    T add(K requestTo);
}