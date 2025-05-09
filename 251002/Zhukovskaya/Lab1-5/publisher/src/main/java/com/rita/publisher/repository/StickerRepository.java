package com.rita.publisher.repository;

import com.rita.publisher.model.Sticker;
import com.rita.publisher.model.Tweet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StickerRepository extends Repo<Sticker> {
}
