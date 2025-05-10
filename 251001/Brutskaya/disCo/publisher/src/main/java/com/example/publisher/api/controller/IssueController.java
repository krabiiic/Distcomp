package com.example.publisher.api.controller;

import com.example.publisher.api.dto.request.IssueRequestTo;
import com.example.publisher.api.dto.responce.IssueResponseTo;
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

public interface IssueController {

    @PostMapping("/api/v1.0/issues")
    @ResponseStatus(HttpStatus.CREATED)
    IssueResponseTo create(@RequestBody
                           @Valid IssueRequestTo request);

    @GetMapping("/api/v1.0/issues")
    @ResponseStatus(HttpStatus.OK)
    List<IssueResponseTo> getAll();

    @GetMapping("/api/v1.0/issues/{id}")
    @ResponseStatus(HttpStatus.OK)
    IssueResponseTo getById(@PathVariable Long id);

    @PutMapping("/api/v1.0/issues")
    @ResponseStatus(HttpStatus.OK)
    IssueResponseTo update(@RequestBody
                           @Valid IssueRequestTo request);

    @DeleteMapping("/api/v1.0/issues/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id);
}
