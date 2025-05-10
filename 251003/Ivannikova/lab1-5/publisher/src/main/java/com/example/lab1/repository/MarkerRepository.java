package com.example.lab1.repository;

import com.example.lab1.model.Marker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkerRepository extends JpaRepository<Marker, Long> {
    Optional<Marker> findByName(String name);
}
