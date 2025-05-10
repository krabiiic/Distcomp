package com.example.rv1.controller;

import com.example.rv1.dto.MarkDTO;
import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Mark;
import com.example.rv1.service.MarkService;
import com.example.rv1.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/marks")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;
    @PostMapping
    public ResponseEntity<MarkDTO> createUser(@Valid @RequestBody MarkDTO userDTO) {
        MarkDTO dto = markService.createMark(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MarkDTO> deleteUser(@PathVariable int id) throws Exception {
        MarkDTO dto = markService.deleteMark(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<MarkDTO> getUser(@PathVariable int id){
        MarkDTO dto = markService.getMark(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<MarkDTO>> getUser(){
        List<MarkDTO> dto = markService.getMarks();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<MarkDTO> updateUser(@Valid @RequestBody MarkDTO userDTO){
        markService.updateMark(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
