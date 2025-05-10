package com.example.JPA2.Impl.CRUD;

import com.example.JPA2.Impl.Entity.Mark;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MarkCRUD implements CRUD<Mark> {
    private final Map<Long, Mark> MarkMemory = new HashMap<>();

    @Override
    public Mark get(long id) {
        Mark mark = MarkMemory.get(id);
        if (mark != null) {
            mark.setId(id);
        }
        return mark;
    }

    @Override
    public List<Mark> getAll() {
        List<Mark> markList = new ArrayList<>();
        for (Long key : MarkMemory.keySet()) {
            Mark mark = MarkMemory.get(key);
            mark.setId(key);
            markList.add(mark);
        }
        return markList;
    }

    @Override
    public Mark delete(long id) {
        return MarkMemory.remove(id);
    }

    @Override
    public Mark insert(Mark insertObject) {
        MarkMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Mark updatingValue) {
        return MarkMemory.replace(updatingValue.getId(), MarkMemory.get(updatingValue.getId()), updatingValue);
    }
}
