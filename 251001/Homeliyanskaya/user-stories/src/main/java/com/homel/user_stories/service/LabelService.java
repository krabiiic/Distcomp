package com.homel.user_stories.service;

import com.homel.user_stories.dto.LabelRequestTo;
import com.homel.user_stories.dto.LabelResponseTo;
import com.homel.user_stories.exception.EntityNotFoundException;
import com.homel.user_stories.mapper.LabelMapper;
import com.homel.user_stories.model.Label;
import com.homel.user_stories.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public LabelResponseTo createLabel(LabelRequestTo labelRequest) {
        Label label = LabelMapper.INSTANCE.toEntity(labelRequest);
        Label savedLabel = labelRepository.save(label);
        return LabelMapper.INSTANCE.toResponse(savedLabel);
    }

    public LabelResponseTo getLabel(Long id) {
        return labelRepository.findById(id)
                .map(LabelMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));
    }

    public List<LabelResponseTo> getAllLabels() {
        return labelRepository.findAll().stream()
                .map(LabelMapper.INSTANCE::toResponse)
                .toList();
    }

    public void deleteLabel(Long id) {
        labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label with id " + id + " not found"));

        labelRepository.deleteById(id);
    }

    public LabelResponseTo updateLabel(LabelRequestTo labelRequest) {
        Label existingLabel = labelRepository.findById(labelRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));

        existingLabel.setName(labelRequest.getName());

        Label updatedLabel = labelRepository.save(existingLabel);

        return LabelMapper.INSTANCE.toResponse(updatedLabel);
    }
}
