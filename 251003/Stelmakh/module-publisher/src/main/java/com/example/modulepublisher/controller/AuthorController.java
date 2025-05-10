package com.example.modulepublisher.controller;

import com.example.modulepublisher.dto.AuthorDTO;
import com.example.modulepublisher.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService userService;
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO userDTO) {
        AuthorDTO dto = userService.createAuthor(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDTO> deleteAuthor(@PathVariable int id) throws Exception {
        AuthorDTO dto = userService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable int id){
        AuthorDTO dto = userService.getAuthor(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<AuthorDTO>> getAuthor(){
        List<AuthorDTO> dto = userService.getAuthors();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<AuthorDTO> updateAuthor(@Valid @RequestBody AuthorDTO userDTO){
        userService.updateAuthor(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
