package org.ikrotsyuk.bsuir.modulepublisher.repository;

import org.ikrotsyuk.bsuir.modulepublisher.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Optional<ArticleEntity> findByTitle(String title);

    boolean existsById(Long id);
}
