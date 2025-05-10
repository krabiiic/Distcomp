package org.example.Impl.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.stream.Stream;


@NoRepositoryBean
public interface Repository<T> extends JpaRepository<T,Long> {
    default Stream<T> getAll() {
        return findAll().stream();
    }

    default Optional<T> get(long id) {
        return findById(id);
    }

    default Optional<T> add(T entity) {
        return Optional.of(save(entity));
    }

    default Optional<T> update(T entity) {
        return Optional.of(save(entity));
    }

    default void delete(long id) {
        deleteById(id);
    }
}
