package com.example.Rest1.Impl.Controllers;

import com.example.Rest1.Impl.DTO.MarkResponseTo;
import com.example.Rest1.Impl.DTO.MarkRequestTo;
import com.example.Rest1.Impl.Services.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class MarkController {
    @Autowired
    private MarkService markService;

    @GetMapping("/marks")
    public ResponseEntity<List<MarkResponseTo>> getAllMarks() {
        List<MarkResponseTo> markResponseToList = markService.getAll();
        return new ResponseEntity<>(markResponseToList, HttpStatus.OK);
    }

    @GetMapping("/marks/{id}")
    public ResponseEntity<MarkResponseTo> getMark(@PathVariable long id) {
        MarkResponseTo MarkResponseTo = markService.get(id);
        return new ResponseEntity<>(MarkResponseTo, MarkResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/marks")
    public ResponseEntity<MarkResponseTo> createMark(@RequestBody MarkRequestTo MarkTo) {
        MarkResponseTo addedMark = markService.add(MarkTo);
        return new ResponseEntity<>(addedMark, HttpStatus.CREATED);
    }

    @DeleteMapping("/marks/{id}")
    public ResponseEntity<MarkResponseTo> deleteMark(@PathVariable long id) {
        MarkResponseTo deleteMark = markService.delete(id);
        return new ResponseEntity<>(deleteMark, deleteMark == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/marks")
    public ResponseEntity<MarkResponseTo> updateMark(@RequestBody MarkRequestTo MarkRequestTo) {
        MarkResponseTo MarkResponseTo = markService.update(MarkRequestTo);
        return new ResponseEntity<>(MarkResponseTo, MarkResponseTo.getName() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
