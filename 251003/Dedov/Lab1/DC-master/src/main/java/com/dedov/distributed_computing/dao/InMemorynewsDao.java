package com.dedov.distributed_computing.dao;

import com.dedov.distributed_computing.model.news;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemorynewsDao {
    private final Map<Long, news> newss = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public news save(news news) {
        if (news.getId()==0) {
            news.setId(idGenerator.getAndIncrement());
        }
        newss.put(news.getId(), news);
        return news;
    }

    public List<news> findAll() {
        return new ArrayList<>(newss.values());
    }
    public Optional<news> findById(long id) {
        return Optional.ofNullable(newss.get(id));
    }

    public void deleteById(long id) {
        newss.remove(id);
    }
}
