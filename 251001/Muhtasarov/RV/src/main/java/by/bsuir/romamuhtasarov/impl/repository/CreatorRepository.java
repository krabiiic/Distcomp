package by.bsuir.romamuhtasarov.impl.repository;

import by.bsuir.romamuhtasarov.api.InMemoryRepository;
import by.bsuir.romamuhtasarov.impl.bean.Creator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CreatorRepository implements InMemoryRepository<Creator> {

    private final Map<Long, Creator> CreatorMemory = new HashMap<>();

    @Override
    public Creator get(long id) {
        Creator creator = CreatorMemory.get(id);
        if (creator != null) {
            creator.setId(id);
        }
        return creator;
    }

    @Override
    public List<Creator> getAll() {
        List<Creator> creatorList = new ArrayList<>();
        for (Long key : CreatorMemory.keySet()) {
            Creator creator = CreatorMemory.get(key);
            creator.setId(key);
            creatorList.add(creator);
        }
        return creatorList;
    }

    @Override
    public Creator delete(long id) {
        return CreatorMemory.remove(id);
    }

    @Override
    public Creator insert(Creator insertObject) {
        CreatorMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Creator updatingValue) {
        return CreatorMemory.replace(updatingValue.getId(), CreatorMemory.get(updatingValue.getId()), updatingValue);
    }

}