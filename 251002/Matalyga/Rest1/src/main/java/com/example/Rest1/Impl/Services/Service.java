package com.example.Rest1.Impl.Services;

import java.util.List;

public interface Service<T, K> {
    List<T> getAll();
    T update(K requestTo);
    T get(long id);
    T delete(long id);
    T add(K requestTo);
}