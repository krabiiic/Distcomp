package org.example.Impl.Controllers;

import jakarta.validation.Valid;
import org.example.Impl.DTO.MarkRequestTo;
import org.example.Impl.DTO.MarkResponseTo;
import org.example.Impl.Services.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0")
public class MarkController {

    @Autowired
    private MarkService markService;

    @GetMapping("/marks")
    public Collection<MarkResponseTo> getAllMarks() {
        return markService.getAll();
    }

    @GetMapping("/marks/{id}")
    public ResponseEntity<MarkResponseTo> getMark(@PathVariable long id) {
        try{
            MarkResponseTo mark = markService.get(id);
            return new ResponseEntity<>(mark, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/marks")
    public ResponseEntity<MarkResponseTo> createMark(@Valid @RequestBody MarkRequestTo mark) {
        System.out.println(mark.getName());
        MarkResponseTo addedMark = markService.add(mark);
        return new ResponseEntity<>(addedMark, HttpStatus.CREATED);
    }

    @DeleteMapping("/marks/{id}")
    public ResponseEntity<?> deleteMark(@PathVariable long id) {
        MarkResponseTo mark = markService.delete(id);
        if (mark == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/marks")
    public ResponseEntity<MarkResponseTo> updatePost(@Valid @RequestBody MarkRequestTo markToUpdate) {
        MarkResponseTo markResponseTo = markService.update(markToUpdate);
        return new ResponseEntity<>(markResponseTo, HttpStatus.OK);
    }

}
