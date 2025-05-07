package com.dc.repository;

import com.dc.model.blo.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {
    Page<Message> findAll(Pageable pageable);

}
