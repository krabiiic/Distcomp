package com.example.restservice.service;

import com.example.restservice.dto.request.MarkRequestTo;
import com.example.restservice.dto.response.MarkResponseTo;
import com.example.restservice.model.Mark;
import com.example.restservice.repository.MarkRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MarkService {
    private final MarkRepository markRepository;
    private final ModelMapper modelMapper;

    public List<MarkResponseTo> getAll() {
        return markRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MarkResponseTo getById(Long id) {
        return markRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));
    }

    public MarkResponseTo create(MarkRequestTo request) {
        Mark mark = modelMapper.map(request, Mark.class);
        Mark saved = markRepository.save(mark);
        return toResponse(saved);
    }

    public MarkResponseTo update(MarkRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("Mark not found");
        }

        Mark existing = markRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));

        modelMapper.map(request, existing);
        Mark updated = markRepository.save(existing);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (markRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        markRepository.deleteById(id);
    }

    private MarkResponseTo toResponse(Mark mark) {
        return modelMapper.map(mark, MarkResponseTo.class);
    }
}
