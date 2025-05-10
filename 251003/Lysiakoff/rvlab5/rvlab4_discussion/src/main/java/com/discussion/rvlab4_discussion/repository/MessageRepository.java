package com.discussion.rvlab4_discussion.repository;

import com.discussion.rvlab4_discussion.entity.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends BaseRepository<Message, Long> {}
