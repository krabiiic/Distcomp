package com.example.publisher.api.controller;

import com.example.publisher.api.dto.request.LabelRequestTo;
import com.example.publisher.api.dto.responce.LabelResponseTo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface LabelController {

    @PostMapping("/api/v1.0/labels")
    @ResponseStatus(HttpStatus.CREATED)
    LabelResponseTo create(@RequestBody
                           @Valid LabelRequestTo request);

    @GetMapping("/api/v1.0/labels")
    @ResponseStatus(HttpStatus.OK)
    List<LabelResponseTo> getAll();

    @GetMapping("/api/v1.0/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    LabelResponseTo getById(@PathVariable Long id);

    @PutMapping("/api/v1.0/labels")
    @ResponseStatus(HttpStatus.OK)
    LabelResponseTo update(@RequestBody
                           @Valid LabelRequestTo request);

    @DeleteMapping("/api/v1.0/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id);
}
