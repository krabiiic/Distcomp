package com.example.rv1.controller;

import com.example.rv1.dto.EditorDTO;
import com.example.rv1.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/editors")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;
    @PostMapping
    public ResponseEntity<EditorDTO> createEditor(@Valid @RequestBody EditorDTO editorDTO) {
        EditorDTO dto = editorService.createEditor(editorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<EditorDTO> deleteEditor(@PathVariable int id) throws Exception {
        EditorDTO dto = editorService.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<EditorDTO> getEditor(@PathVariable int id){
        EditorDTO dto = editorService.getEditor(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<EditorDTO>> getEditor(){
        List<EditorDTO> dto = editorService.getEditors();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<EditorDTO> updateEditor(@Valid @RequestBody EditorDTO editorDTO){
        editorService.updateEditor(editorDTO);
        return ResponseEntity.status(HttpStatus.OK).body(editorDTO);
    }
}
