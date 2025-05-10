package com.example.rv1.repository;

import com.example.rv1.entity.Label;
import com.example.rv1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {
    Optional<Label> findUserById(int id);
}
