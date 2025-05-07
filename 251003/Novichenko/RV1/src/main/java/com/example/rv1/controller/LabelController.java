package com.example.rv1.controller;

import com.example.rv1.dto.LabelDTO;
import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Label;
import com.example.rv1.service.LabelService;
import com.example.rv1.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;
    @PostMapping
    public ResponseEntity<LabelDTO> createUser(@Valid @RequestBody LabelDTO userDTO) {
        LabelDTO dto = labelService.createLabel(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<LabelDTO> deleteUser(@PathVariable int id) throws Exception {
        LabelDTO dto = labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getUser(@PathVariable int id){
        LabelDTO dto = labelService.getLabel(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<LabelDTO>> getUser(){
        List<LabelDTO> dto = labelService.getLabels();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<LabelDTO> updateUser(@Valid @RequestBody LabelDTO userDTO){
        labelService.updateLabel(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
