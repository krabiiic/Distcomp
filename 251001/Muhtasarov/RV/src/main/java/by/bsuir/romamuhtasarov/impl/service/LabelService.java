package by.bsuir.romamuhtasarov.impl.service;


import by.bsuir.romamuhtasarov.api.Service;
import by.bsuir.romamuhtasarov.api.LabelMapper;
import by.bsuir.romamuhtasarov.impl.bean.Label;
import by.bsuir.romamuhtasarov.impl.dto.*;
import by.bsuir.romamuhtasarov.impl.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LabelService implements Service<LabelResponseTo, LabelRequestTo> {
    @Autowired
    private LabelRepository LabelRepository;

    public LabelService() {

    }

    public List<LabelResponseTo> getAll() {
        List<Label> LabelList = LabelRepository.getAll();
        List<LabelResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < LabelList.size(); i++) {
            resultList.add(LabelMapper.INSTANCE.LabelToLabelResponseTo(LabelList.get(i)));
        }
        return resultList;
    }

    public LabelResponseTo update(LabelRequestTo updatingLabel) {
        Label Label = LabelMapper.INSTANCE.LabelRequestToToLabel(updatingLabel);
        if (validateLabel(Label)) {
            boolean result = LabelRepository.update(Label);
            LabelResponseTo responseTo = result ? LabelMapper.INSTANCE.LabelToLabelResponseTo(Label) : null;
            return responseTo;
        } else return new LabelResponseTo();
        //return responseTo;
    }

    public LabelResponseTo get(long id) {
        return LabelMapper.INSTANCE.LabelToLabelResponseTo(LabelRepository.get(id));
    }

    public LabelResponseTo delete(long id) {
        return LabelMapper.INSTANCE.LabelToLabelResponseTo(LabelRepository.delete(id));
    }

    public LabelResponseTo add(LabelRequestTo LabelRequestTo) {
        Label Label = LabelMapper.INSTANCE.LabelRequestToToLabel(LabelRequestTo);
        return LabelMapper.INSTANCE.LabelToLabelResponseTo(LabelRepository.insert(Label));
    }

    private boolean validateLabel(Label Label) {
        String content = Label.getName();
        if (content.length() >= 2 && content.length() <= 2048) return true;
        return false;
    }
}