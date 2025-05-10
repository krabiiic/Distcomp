package com.dedov.distributed_computing.controller;

import com.dedov.distributed_computing.dto.request.editorRequestTo;
import com.dedov.distributed_computing.dto.response.editorResponseTo;
import com.dedov.distributed_computing.exception.DuplicateFieldException;
import com.dedov.distributed_computing.exception.EntityNotFoundException;
import com.dedov.distributed_computing.service.editorService;
import com.dedov.distributed_computing.validation.groups.OnCreateOrUpdate;
import com.dedov.distributed_computing.validation.groups.OnPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/editors")
public class editorController {

    private final editorService editorService;

    @Autowired
    public editorController(editorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    public ResponseEntity<List<editorResponseTo>> findAll() {
        return new ResponseEntity<>(editorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            editorResponseTo response = editorService.findById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-news/{newsId}")
    public ResponseEntity<?> findBynewsId(@PathVariable long newsId) {
        try {
            editorResponseTo response = editorService.findBynewsId(newsId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Validated(OnCreateOrUpdate.class) editorRequestTo editorRequestTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(editorService.save(editorRequestTo), HttpStatus.CREATED);
        } catch (DuplicateFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Validated(OnCreateOrUpdate.class) editorRequestTo editorRequestTo,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            editorResponseTo updatededitor = editorService.update(editorRequestTo);
            return new ResponseEntity<>(updatededitor, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicateFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@RequestBody @Validated(OnPatch.class) editorRequestTo editorRequestTo,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            editorResponseTo updatededitor = editorService.partialUpdate(editorRequestTo);
            return new ResponseEntity<>(updatededitor, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DuplicateFieldException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            editorService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
