package com.example.publisher.api.controller.impl;

import com.example.publisher.api.controller.LabelController;
import com.example.publisher.api.dto.request.LabelRequestTo;
import com.example.publisher.api.dto.responce.LabelResponseTo;
import com.example.publisher.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LabelControllerImpl implements LabelController {

    private final LabelService labelService;

    @Autowired
    public LabelControllerImpl(LabelService labelService) {
        this.labelService = labelService;
    }

    @Override
    public LabelResponseTo create(LabelRequestTo request) {
        return labelService.create(request);
    }

    @Override
    public List<LabelResponseTo> getAll() {
        return labelService.getAll();
    }

    @Override
    public LabelResponseTo getById(Long id) {
        return labelService.getById(id);
    }

    @Override
    public LabelResponseTo update(LabelRequestTo request) {
        return labelService.update(request);
    }

    @Override
    public void delete(Long id) {
        labelService.delete(id);
    }
}
