package com.example.lab1.service;

import com.example.lab1.dto.MarkerRequestTo;
import com.example.lab1.dto.MarkerResponseTo;
import com.example.lab1.exception.NotFoundException;
import com.example.lab1.model.Marker;
import com.example.lab1.repository.MarkerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarkerService {

    private final MarkerRepository markRepository;
    
    public MarkerService(MarkerRepository markRepository) {
        this.markRepository = markRepository;
    }
    
    public MarkerResponseTo createMark(MarkerRequestTo request) {
        Marker mark = new Marker();
        mark.setName(request.getName());
        Marker saved = markRepository.save(mark);
        return toDto(saved);
    }
    
    public List<MarkerResponseTo> getAllMarks() {
        return markRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Marker findOrCreateMark(String name) {
        return markRepository.findByName(name)
                .orElseGet(() -> {
                    Marker newMark = new Marker();
                    newMark.setName(name);
                    return markRepository.save(newMark);
                });
    }

    public MarkerResponseTo getMarkById(Long id) {
        Marker mark = markRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mark not found", 40404));
        return toDto(mark);
    }
    
    public MarkerResponseTo updateMark(Long id, MarkerRequestTo request) {
        Marker mark = markRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mark not found", 40404));
        mark.setName(request.getName());
        Marker updated = markRepository.save(mark);
        return toDto(updated);
    }
    
    public void deleteMark(Long id) {
        if(!markRepository.existsById(id)) {
            throw new NotFoundException("Mark not found", 40404);
        }
        markRepository.deleteById(id);
    }
    
    private MarkerResponseTo toDto(Marker mark) {
        MarkerResponseTo dto = new MarkerResponseTo();
        dto.setId(mark.getId());
        dto.setName(mark.getName());
        return dto;
    }
}
