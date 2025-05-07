package org.ikrotsyuk.bsuir.modulepublisher.repository;

import org.ikrotsyuk.bsuir.modulepublisher.entity.StickerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends JpaRepository<StickerEntity, Long> {
    void deleteByName(String name);
}
