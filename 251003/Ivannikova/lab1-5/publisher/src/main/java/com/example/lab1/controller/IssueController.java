package com.example.lab1.controller;

import com.example.lab1.dto.IssueRequestTo;
import com.example.lab1.dto.IssueResponseTo;
import com.example.lab1.service.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/issues")
public class IssueController {

    private final IssueService issueService;
    
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }
    
    @PostMapping
    public ResponseEntity<IssueResponseTo> createIssue(@Valid @RequestBody IssueRequestTo request) {
        IssueResponseTo response = issueService.createIssue(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<IssueResponseTo>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseTo> getIssueById(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }
    
    @PutMapping
    public ResponseEntity<IssueResponseTo> updateIssue(@Valid @RequestBody IssueRequestTo request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("ID must be provided");
        }
        return ResponseEntity.ok(issueService.updateIssue(request.getId(), request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
}
