package com.lab.labDCP.repository;

import com.lab.labDCP.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepositoryJPA extends JpaRepository<Sticker, Long> {
}
