package org.ikrotsyuk.bsuir.modulepublisher.repository;

import org.ikrotsyuk.bsuir.modulepublisher.entity.WriterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WriterRepository extends JpaRepository<WriterEntity, Long> {
    Optional<WriterEntity> findByLogin(String login);

    boolean existsById(Long id);
}
