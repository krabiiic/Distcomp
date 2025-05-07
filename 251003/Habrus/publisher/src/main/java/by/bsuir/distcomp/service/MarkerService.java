package by.bsuir.distcomp.service;

import by.bsuir.distcomp.dto.mapper.MarkerMapper;
import by.bsuir.distcomp.dto.request.MarkerRequestTo;
import by.bsuir.distcomp.dto.response.MarkerResponseTo;
import by.bsuir.distcomp.repository.MarkerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerMapper markerMapper;

    public MarkerService(MarkerRepository markerRepository, MarkerMapper markerMapper) {
        this.markerRepository = markerRepository;
        this.markerMapper = markerMapper;
    }

    @Cacheable (value = "markers", key = "'all'")
    public List<MarkerResponseTo> getAllMarkers() {
        return markerRepository.findAll().stream().map(markerMapper::toDto).toList();
    }

    @Cacheable (value = "markers", key = "#id")
    public MarkerResponseTo getMarkerById(Long id) {
        return markerRepository.findById(id)
                .map(markerMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Marker with id: " + id + " not found"));
    }

    @Transactional
    @CacheEvict(value = "markers", key = "'all'")
    public MarkerResponseTo createMarker(MarkerRequestTo markerRequestTo) {
        return markerMapper.toDto(markerRepository.save(markerMapper.toEntity(markerRequestTo)));
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "markers", key = "#markerRequestTo.id"),
            evict = @CacheEvict(value = "markers", key = "'all'")
    )
    public MarkerResponseTo updateMarker(MarkerRequestTo markerRequestTo) {
        Long id = markerRequestTo.getId();
        markerRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Marker with id: " + id + " not found"));
        return markerMapper.toDto(markerRepository.save(markerMapper.toEntity(markerRequestTo)));
    }

    @Transactional
    @Caching (
            evict = {
                    @CacheEvict(value = "markers", key = "#id"),
                    @CacheEvict(value = "markers", key = "'all'")
            }
    )
    public void deleteMarker(Long id) {
        markerRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Marker with id: " + id + " not found"));
        markerRepository.deleteById(id);
    }
    
}
