package com.example.publisher.controller.exception;

import com.example.publisher.service.exception.DuplicateLoginException;
import com.example.publisher.service.exception.DuplicateTitleException;
import com.example.publisher.service.exception.TopicNotFoundException;
import com.example.publisher.service.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> notFoundException(EntityNotFoundException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public ResponseEntity<Object> duplicateLoginException(DuplicateLoginException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UserNotFoundException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<Object> topicNotFoundException(TopicNotFoundException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateTitleException.class)
    public ResponseEntity<Object> duplicateTitleException(DuplicateTitleException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

}
