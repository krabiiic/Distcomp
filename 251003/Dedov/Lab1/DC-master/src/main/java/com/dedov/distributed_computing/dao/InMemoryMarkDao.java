package com.dedov.distributed_computing.dao;

import com.dedov.distributed_computing.model.Mark;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryMarkDao {
    private final Map<Long, Mark> marks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Mark save(Mark mark) {
        if (mark.getId()==0) {
            mark.setId(idGenerator.getAndIncrement());
        }
        marks.put(mark.getId(), mark);
        return mark;
    }

    public List<Mark> findAll() {
        return new ArrayList<>(marks.values());
    }
    public Optional<Mark> findById(long id) {
        return Optional.ofNullable(marks.get(id));
    }

    public void deleteById(long id) {
        marks.remove(id);
    }
}
