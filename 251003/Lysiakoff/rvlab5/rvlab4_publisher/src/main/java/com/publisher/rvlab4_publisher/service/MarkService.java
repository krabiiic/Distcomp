package com.publisher.rvlab4_publisher.service;

import com.publisher.rvlab4_publisher.dto.MarkRequestTo;
import com.publisher.rvlab4_publisher.dto.MarkResponseTo;
import com.publisher.rvlab4_publisher.entity.Mark;
import com.publisher.rvlab4_publisher.repository.MarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;

    public List<MarkResponseTo> getAllMarks() {
        return markRepository.findAll().stream()
                .map(mark -> new MarkResponseTo(mark.getId(), mark.getName()))
                .collect(Collectors.toList());
    }

    public MarkResponseTo getMarkById(Long id) {
        return markRepository.findById(id)
                .map(mark -> new MarkResponseTo(mark.getId(), mark.getName()))
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));
    }

    public Optional<Mark> getMarkByName(String name) {
        return markRepository.findByName(name);
    }

    public MarkResponseTo createMark(MarkRequestTo request) {
        Mark mark = new Mark();
        mark.setName(request.getName());

        Mark savedMark = markRepository.save(mark);

        return new MarkResponseTo(
                savedMark.getId(),
                savedMark.getName()
        );
    }

    public MarkResponseTo updateMark(Long id, MarkRequestTo request) {
        Mark mark = markRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));
        mark.setName(request.getName());
        markRepository.save(mark);
        return new MarkResponseTo(id, mark.getName());
    }

    public void deleteMark(Long id) {
        Mark mark = markRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Mark no found"));
        markRepository.deleteById(id);
    }
}
