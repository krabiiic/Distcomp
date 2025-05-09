package com.rita.publisher.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.stream.Stream;
@NoRepositoryBean
public interface Repo<T> extends JpaRepository<T,Long> {
    default Stream<T> getAll(){
        return findAll().stream();
    }
    default Optional<T> get(Long id){
        return findById(id);
    }
    default Optional<T> create(T input){
        return Optional.of(save(input));
    }
    default Optional<T> update(T input){
        return Optional.of(save(input));
    }
    default void delete(Long id){
        if(existsById(id))
            deleteById(id);
        else throw new EntityNotFoundException("No such element with id="+id);
    }
}
