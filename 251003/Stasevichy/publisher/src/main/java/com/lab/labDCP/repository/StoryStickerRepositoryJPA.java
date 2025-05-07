package com.lab.labDCP.repository;

import com.lab.labDCP.entity.StorySticker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryStickerRepositoryJPA extends JpaRepository<StorySticker, Long> {
        @Query("SELECT ss.sticker_id FROM StorySticker ss WHERE ss.story_id = :story_id")
        List<Long> findStickerIdsByStoryId(@Param("story_id") Long story_id);

        @Modifying
        @Transactional
        @Query("DELETE FROM StorySticker ss WHERE ss.story_id = :story_id")
        void deleteByStoryId(@Param("story_id") Long storyId);
}
