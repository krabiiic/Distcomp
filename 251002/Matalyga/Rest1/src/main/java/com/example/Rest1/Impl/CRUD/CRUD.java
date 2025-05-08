package com.example.Rest1.Impl.CRUD;

import java.util.List;

public interface CRUD<T> {
    List<T> getAll();
    T get(long id);
    T insert(T insertObject);
    boolean update(T updatingValue);
    T delete(long id);
}

