package com.example.publisher.service;

import com.example.publisher.api.dto.request.LabelRequestTo;
import com.example.publisher.api.dto.responce.LabelResponseTo;
import com.example.publisher.mapper.LabelMapper;
import com.example.publisher.model.Label;
import com.example.publisher.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Autowired
    public LabelService(LabelRepository labelRepository, LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    public LabelResponseTo create(LabelRequestTo request) {
        Label label = labelMapper.fromRequestToEntity(request);
        return labelMapper.fromEntityToResponse(labelRepository.save(label));
    }

    public List<LabelResponseTo> getAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::fromEntityToResponse)
                .toList();
    }


    public LabelResponseTo getById(Long id) {
        return labelRepository.findById(id)
                .map(labelMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public LabelResponseTo update(LabelRequestTo request) {
        Label entity = labelRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        labelMapper.updateEntity(entity, request);
        return labelMapper.fromEntityToResponse(labelRepository.save(entity));

    }

    public void delete(Long id) {
        if (labelRepository.existsById(id)) {
            labelRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
