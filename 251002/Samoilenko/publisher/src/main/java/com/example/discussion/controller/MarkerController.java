package com.example.discussion.controller;

import com.example.discussion.dto.MarkerRequestTo;
import com.example.discussion.dto.MarkerResponseTo;
import com.example.discussion.service.MarkerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/markers")
public class MarkerController {

    private final MarkerService markerService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public MarkerController(MarkerService markerService, RedisTemplate<String, Object> redisTemplate) {
        this.markerService = markerService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getAllMarkers() {
        try {
            String cacheKey = "allMarkers";
            List<MarkerResponseTo> markers = (List<MarkerResponseTo>) redisTemplate.opsForValue().get(cacheKey);

            if (markers == null) {
                markers = markerService.findAll();
                redisTemplate.opsForValue().set(cacheKey, markers, Duration.ofMinutes(10));
            }
            return new ResponseEntity<>(markers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving markers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMarkerById(@PathVariable Long id) {
        try {
            String cacheKey = "marker:" + id;
            MarkerResponseTo marker = (MarkerResponseTo) redisTemplate.opsForValue().get(cacheKey);

            if (marker == null) {
                marker = markerService.findById(id);
                if (marker != null) {
                    redisTemplate.opsForValue().set(cacheKey, marker, Duration.ofMinutes(10));
                } else {
                    return new ResponseEntity<>("Marker not found", HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(marker, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving marker", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createMarker(@Valid @RequestBody MarkerRequestTo markerRequestTo) {
        try {
            MarkerResponseTo createdMarker = markerService.save(markerRequestTo);
            redisTemplate.delete("allMarkers");
            return new ResponseEntity<>(createdMarker, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating marker", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMarker(@Valid @RequestBody MarkerRequestTo markerRequestTo) {
        try {
            MarkerResponseTo updatedMarker = markerService.update(markerRequestTo);
            if (updatedMarker != null) {
                redisTemplate.delete("allMarkers");
                redisTemplate.delete("marker:" + updatedMarker.getId());
                return new ResponseEntity<>(updatedMarker, HttpStatus.OK);
            }
            return new ResponseEntity<>("Marker not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating marker", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarker(@PathVariable Long id) {
        markerService.deleteById(id);
        redisTemplate.delete("allMarkers");
        redisTemplate.delete("marker:" + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}