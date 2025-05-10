package com.example.rv1.repository;

import com.example.rv1.entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Integer> {
    Optional<Editor> findEditorById(int id);
    Optional<Editor> findEditorByLogin(String login);
}
