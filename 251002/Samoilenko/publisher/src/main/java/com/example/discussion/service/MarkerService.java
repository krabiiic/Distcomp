package com.example.discussion.service;

import com.example.discussion.dto.MarkerRequestTo;
import com.example.discussion.dto.MarkerResponseTo;
import com.example.discussion.model.Marker;
import com.example.discussion.repository.MarkerRepository;
import com.example.discussion.service.mapper.MarkerMapper;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerMapper markerMapper = Mappers.getMapper(MarkerMapper.class);

    @Autowired
    public MarkerService(MarkerRepository markerRepository) {
        this.markerRepository = markerRepository;
    }

    public List<MarkerResponseTo> findAll() {
        return markerRepository.findAll().stream()
                .map(markerMapper::toDto)
                .collect(Collectors.toList());
    }

    public MarkerResponseTo findById(Long id) {
        Optional<Marker> marker = markerRepository.findById(id);
        return marker.map(markerMapper::toDto).orElse(null);
    }

    public MarkerResponseTo save(MarkerRequestTo markerRequestTo) {
        Marker marker = markerMapper.toEntity(markerRequestTo);
        Marker savedMarker = markerRepository.save(marker);
        return markerMapper.toDto(savedMarker);
    }

    public MarkerResponseTo update(MarkerRequestTo markerRequestTo) {
        Marker existingMarker = markerRepository.findById(markerRequestTo.getId()).orElseThrow(() -> new RuntimeException("Marker not found"));
        markerMapper.updateEntityFromDto(markerRequestTo, existingMarker);
        Marker updatedMarker = markerRepository.save(existingMarker);
        return markerMapper.toDto(updatedMarker);
    }

    public void deleteById(Long id) {
        if (!markerRepository.existsById(id)) {
            throw new EntityNotFoundException("Marker not found with id " + id);
        }
        markerRepository.deleteById(id);
    }
}