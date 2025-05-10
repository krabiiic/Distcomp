package com.dedov.distributed_computing.controller;

import com.dedov.distributed_computing.dto.request.newsRequestTo;
import com.dedov.distributed_computing.dto.response.newsResponseTo;
import com.dedov.distributed_computing.dto.response.MessageResponseTo;
import com.dedov.distributed_computing.exception.EntityNotFoundException;
import com.dedov.distributed_computing.service.newsService;
import com.dedov.distributed_computing.service.MessageService;
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
@RequestMapping("/api/v1.0/news")
public class newsController {

    private final newsService newsService;
    private final MessageService messageService;

    @Autowired
    public newsController(newsService newsService, MessageService messageService) {
        this.newsService = newsService;
        this.messageService = messageService;
    }


    @GetMapping
    public ResponseEntity<List<newsResponseTo>> findAll() {
        return new ResponseEntity<>(newsService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            newsResponseTo response = newsService.findById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponseTo>> findMessagesBynewsId(@PathVariable long id) {

        List<MessageResponseTo> messages = messageService.findBynewsId(id);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Validated(OnCreateOrUpdate.class) newsRequestTo newsRequestTo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newsService.save(newsRequestTo), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Validated(OnCreateOrUpdate.class) newsRequestTo newsRequestTo,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            newsResponseTo updatednews = newsService.update(newsRequestTo);
            return new ResponseEntity<>(updatednews, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping
    public ResponseEntity<?> partialUpdate(@RequestBody @Validated(OnPatch.class) newsRequestTo newsRequestTo,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            newsResponseTo updatednews = newsService.partialUpdate(newsRequestTo);
            return new ResponseEntity<>(updatednews, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            newsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
