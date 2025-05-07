package by.bsuir.romamuhtasarov.impl.repository;

import by.bsuir.romamuhtasarov.api.InMemoryRepository;
import by.bsuir.romamuhtasarov.impl.bean.Label;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LabelRepository implements InMemoryRepository<Label> {
    private final Map<Long, Label> LabelMemory = new HashMap<>();

    @Override
    public Label get(long id) {
        Label Label = LabelMemory.get(id);
        if (Label != null) {
            Label.setId(id);
        }
        return Label;
    }

    @Override
    public List<Label> getAll() {
        List<Label> LabelList = new ArrayList<>();
        for (Long key : LabelMemory.keySet()) {
            Label Label = LabelMemory.get(key);
            Label.setId(key);
            LabelList.add(Label);
        }
        return LabelList;
    }

    @Override
    public Label delete(long id) {

        return LabelMemory.remove(id);
    }

    @Override
    public Label insert(Label insertObject) {
        LabelMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Label updatingValue) {
        return LabelMemory.replace(updatingValue.getId(), LabelMemory.get(updatingValue.getId()), updatingValue);
    }


}