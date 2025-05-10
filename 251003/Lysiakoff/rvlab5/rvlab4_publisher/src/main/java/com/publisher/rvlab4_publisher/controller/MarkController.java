package com.publisher.rvlab4_publisher.controller;

import com.publisher.rvlab4_publisher.dto.MarkRequestTo;
import com.publisher.rvlab4_publisher.dto.MarkResponseTo;
import com.publisher.rvlab4_publisher.service.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionHandler(ResponseStatusException.class)
    public MarkResponseTo createMark(@RequestBody MarkRequestTo markRequestTo) {
        if (!validateMarkData(markRequestTo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "{}");
        }
        return markService.createMark(markRequestTo);
    }

    private boolean validateMarkData(MarkRequestTo markRequestTo) {

        if (markRequestTo.getName().length() < 2 ||
        markRequestTo.getName().length() > 32) {
            return false;
        }

        return true;
    }

    @GetMapping
    public List<MarkResponseTo> getAllMarks() {
        return markService.getAllMarks();
    }

    @GetMapping("/{id}")
    public MarkResponseTo getMarkById(@PathVariable Long id) {
        return markService.getMarkById(id);
    }

    @PutMapping
    public ResponseEntity<MarkResponseTo> updateNews(@RequestBody MarkRequestTo markRequestTo) {
        System.out.println("Received PUT request with ID: " + markRequestTo.getId());
        try {
            if (markRequestTo.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MarkResponseTo());
            }
            MarkResponseTo updatedMark = markService.updateMark(markRequestTo.getId(), markRequestTo);
            return ResponseEntity.ok(updatedMark);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MarkResponseTo());
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MarkResponseTo());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MarkResponseTo());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMark(@PathVariable Long id) {
        try {
            markService.deleteMark(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

