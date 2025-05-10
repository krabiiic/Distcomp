package com.example.publisher.controller;

import com.example.publisher.dto.TagRequestTo;
import com.example.publisher.dto.TagResponseTo;
import com.example.publisher.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/marks")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TagResponseTo> getTags() {
        return tagService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TagResponseTo getTag(@PathVariable long id) {
        return tagService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TagResponseTo createTag(@Valid @RequestBody TagRequestTo tagRequestTo) {
        return tagService.create(tagRequestTo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public TagResponseTo updateTag(@Valid @RequestBody TagRequestTo tagRequestTo) {
        return tagService.update(tagRequestTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}

