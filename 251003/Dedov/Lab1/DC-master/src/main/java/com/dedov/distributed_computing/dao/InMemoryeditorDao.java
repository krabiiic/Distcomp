package com.dedov.distributed_computing.dao;

import com.dedov.distributed_computing.model.editor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryeditorDao {
    private final Map<Long, editor> editors = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public editor save(editor editor) {
        if (editor.getId()==0) {
            editor.setId(idGenerator.getAndIncrement());
        }
        editors.put(editor.getId(), editor);
        return editor;
    }

    public List<editor> findAll() {
        return new ArrayList<>(editors.values());
    }
    public Optional<editor> findById(long id) {
        return Optional.ofNullable(editors.get(id));
    }
    public Optional<editor> findByLogin(String login) {
        return editors.values()
                .stream()
                .filter(editor -> editor.getLogin().equals(login))
                .findFirst();
    }


    public void deleteById(long id) {
        editors.remove(id);
    }
}
