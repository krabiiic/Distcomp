package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository  extends JpaRepository<Label, Integer>{
    Optional<Label> findUserById(int id);
}
