package com.example.modulepublisher.controller;

import com.example.modulepublisher.service.IssueService;
import com.example.modulepublisher.dto.IssueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/issues")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService tweetService;
    @PostMapping
    public ResponseEntity<IssueDTO> createIssue(@Valid @RequestBody IssueDTO userDTO) {
        IssueDTO dto = tweetService.createIssue(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<IssueDTO> deleteIssue(@PathVariable int id) throws Exception {
        IssueDTO dto = tweetService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<IssueDTO> getIssue(@PathVariable int id){
        IssueDTO dto = tweetService.getIssue(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<IssueDTO>> getIssue(){
        List<IssueDTO> dto = tweetService.getIssues();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<IssueDTO> updateIssue(@Valid @RequestBody IssueDTO userDTO){
        tweetService.updateIssue(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
