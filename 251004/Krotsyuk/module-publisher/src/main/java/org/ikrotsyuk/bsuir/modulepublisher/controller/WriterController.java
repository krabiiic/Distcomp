package org.ikrotsyuk.bsuir.modulepublisher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.WriterRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.WriterResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.service.WriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1.0/writers")
public class WriterController {
    private final WriterService writerService;

    @GetMapping
    public ResponseEntity<List<WriterResponseDTO>> getWriters(){
        return new ResponseEntity<>(writerService.getWriters(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WriterResponseDTO> getWriter(@PathVariable long id){
        return new ResponseEntity<>(writerService.getWriterById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WriterResponseDTO> addWriter(@Valid @RequestBody WriterRequestDTO writerRequestDTO){
        return new ResponseEntity<>(writerService.addWriter(writerRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWriter(@PathVariable Long id){
        return new ResponseEntity<>(writerService.deleteWriter(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WriterResponseDTO> updateWriter(@PathVariable Long id, @Valid @RequestBody WriterRequestDTO writerRequestDTO){
        return new ResponseEntity<>(writerService.updateWriter(id, writerRequestDTO), HttpStatus.OK);
    }
}
