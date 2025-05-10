package com.example.Rest1.Impl.Services;

import com.example.Rest1.Impl.Entity.Mark;
import com.example.Rest1.Impl.DTO.MarkRequestTo;
import com.example.Rest1.Impl.DTO.MarkResponseTo;
import com.example.Rest1.Impl.CRUD.MarkCRUD;
import com.example.Rest1.Mappers.MarkMapper;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkService implements Service<MarkResponseTo, MarkRequestTo> {
    @Autowired
    private MarkCRUD markCRUD;
    public MarkService() {

    }

    public List<MarkResponseTo> getAll() {
        List<Mark> markList = markCRUD.getAll();
        List<MarkResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < markList.size(); i++) {
            resultList.add(MarkMapper.INSTANCE.MarkToMarkResponseTo(markList.get(i)));
        }
        return resultList;
    }

    public MarkResponseTo update(@Valid MarkRequestTo updatingMark) {
        Mark mark = MarkMapper.INSTANCE.MarkRequestToToMark(updatingMark);
        boolean result = markCRUD.update(mark);
        MarkResponseTo responseTo = result ? MarkMapper.INSTANCE.MarkToMarkResponseTo(mark) : null;
        return responseTo;
    }

    public MarkResponseTo get(@Min(0) long id) {
        return MarkMapper.INSTANCE.MarkToMarkResponseTo(markCRUD.get(id));
    }

    public MarkResponseTo delete(@Min(0) long id) {
        return MarkMapper.INSTANCE.MarkToMarkResponseTo(markCRUD.delete(id));
    }

    public MarkResponseTo add(@Valid MarkRequestTo markRequestTo) {
        Mark mark = MarkMapper.INSTANCE.MarkRequestToToMark(markRequestTo);
        return MarkMapper.INSTANCE.MarkToMarkResponseTo(markCRUD.insert(mark));
    }
}