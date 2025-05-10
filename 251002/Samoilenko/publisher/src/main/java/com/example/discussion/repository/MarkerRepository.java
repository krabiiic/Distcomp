package com.example.discussion.repository;

import com.example.discussion.model.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    Optional<Marker> findByName(String name);
}