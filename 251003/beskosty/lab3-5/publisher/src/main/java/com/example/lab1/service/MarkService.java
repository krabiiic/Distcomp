package com.example.lab1.service;

import com.example.lab1.dto.MarkRequestTo;
import com.example.lab1.dto.MarkResponseTo;
import com.example.lab1.exception.NotFoundException;
import com.example.lab1.model.Mark;
import com.example.lab1.repository.MarkRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    
    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }
    
    public MarkResponseTo createMark(MarkRequestTo request) {
        Mark mark = new Mark();
        mark.setName(request.getName());
        Mark saved = markRepository.save(mark);
        return toDto(saved);
    }
    
    public List<MarkResponseTo> getAllMarks() {
        return markRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Mark findOrCreateMark(String name) {
        return markRepository.findByName(name)
                .orElseGet(() -> {
                    Mark newMark = new Mark();
                    newMark.setName(name);
                    return markRepository.save(newMark);
                });
    }

    public MarkResponseTo getMarkById(Long id) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mark not found", 40404));
        return toDto(mark);
    }
    
    public MarkResponseTo updateMark(Long id, MarkRequestTo request) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mark not found", 40404));
        mark.setName(request.getName());
        Mark updated = markRepository.save(mark);
        return toDto(updated);
    }
    
    public void deleteMark(Long id) {
        if(!markRepository.existsById(id)) {
            throw new NotFoundException("Mark not found", 40404);
        }
        markRepository.deleteById(id);
    }
    
    private MarkResponseTo toDto(Mark mark) {
        MarkResponseTo dto = new MarkResponseTo();
        dto.setId(mark.getId());
        dto.setName(mark.getName());
        return dto;
    }
}
