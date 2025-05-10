package com.dedov.distributed_computing.dao;

import com.dedov.distributed_computing.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryMessageDao {
    private final Map<Long, Message> messages = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Message save(Message message) {
        if (message.getId()==0) {
            message.setId(idGenerator.getAndIncrement());
        }
        messages.put(message.getId(), message);
        return message;
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    public List<Message> findBynewsId(long newsId) {
        return messages.values().stream()
                .filter(message -> message.getnewsId() == newsId)
                .collect(Collectors.toList());
    }
    public Optional<Message> findById(long id) {
        return Optional.ofNullable(messages.get(id));
    }

    public void deleteById(long id) {
        messages.remove(id);
    }
}
