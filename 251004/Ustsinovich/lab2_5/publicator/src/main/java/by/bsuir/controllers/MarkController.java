package by.bsuir.controllers;

import by.bsuir.dto.NoteResponseTo;
import by.bsuir.dto.MarkRequestTo;
import by.bsuir.dto.MarkResponseTo;
import by.bsuir.services.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/marks")
public class MarkController {
    @Autowired
    MarkService markService;

    @GetMapping
    public ResponseEntity<List<MarkResponseTo>> getMarks(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ResponseEntity.status(200).body(markService.getMarks(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseTo> getMark(@PathVariable Long id) {
        return ResponseEntity.status(200).body(markService.getMarkById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMark(@PathVariable Long id) {
        markService.deleteMark(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MarkResponseTo> saveMark(@RequestBody MarkRequestTo mark) {
        MarkResponseTo savedMark = markService.saveMark(mark);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMark);
    }

    @PutMapping()
    public ResponseEntity<MarkResponseTo> updateMark(@RequestBody MarkRequestTo mark) {
        return ResponseEntity.status(HttpStatus.OK).body(markService.updateMark(mark));
    }

    @GetMapping("/byIssue/{id}")
    public ResponseEntity<List<MarkResponseTo>> getEditorByIssueId(@PathVariable Long id){
        return ResponseEntity.status(200).body(markService.getMarkByIssueId(id));
    }
}