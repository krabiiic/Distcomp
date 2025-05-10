package org.ex.distributed_computing.controller;

import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.WriterRequestDTO;
import org.ex.distributed_computing.dto.response.WriterResponseDTO;
import org.ex.distributed_computing.service.WriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/writers")
public class WriterController {

  private final WriterService writerService;

  @GetMapping
  public List<WriterResponseDTO> getAllWriters() {
    return writerService.getAllWriters();
  }

  @GetMapping("/{id}")
  public ResponseEntity<WriterResponseDTO> getWriterById(@PathVariable Long id) {
    return ResponseEntity.ok(writerService.getWriterById(id));
  }

  @PostMapping
  public ResponseEntity<WriterResponseDTO> createWriter(@Valid @RequestBody WriterRequestDTO requestDTO) {
    var author = writerService.createWriter(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED.value()).body(author);
  }

  @PutMapping()
  public ResponseEntity<WriterResponseDTO> updateWriter(@Valid @RequestBody WriterRequestDTO dto) {
    return ResponseEntity.ok(writerService.updateWriter(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWriter(@PathVariable Long id) {
    writerService.deleteWriter(id);
    return ResponseEntity.noContent().build();
  }
}
