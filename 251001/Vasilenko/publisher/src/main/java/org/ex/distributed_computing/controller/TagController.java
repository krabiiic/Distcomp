package org.ex.distributed_computing.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.TagRequestDTO;
import org.ex.distributed_computing.dto.response.TagResponseDTO;
import org.ex.distributed_computing.service.TagService;
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
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;

  @GetMapping
  public List<TagResponseDTO> getAllTags() {
    return tagService.getAllTags();
  }

  @GetMapping("/{id}")
  public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
    return ResponseEntity.ok(tagService.getTagById(id));
  }

  @PostMapping
  public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO requestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(requestDTO));
  }

  @PutMapping
  public ResponseEntity<TagResponseDTO> updateTag(@Valid @RequestBody TagRequestDTO requestDTO) {
    return ResponseEntity.ok(tagService.updateTag(requestDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTag(id);
    return ResponseEntity.noContent().build();
  }
}
