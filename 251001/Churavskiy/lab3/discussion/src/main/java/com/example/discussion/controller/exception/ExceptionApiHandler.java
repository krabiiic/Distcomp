package com.example.discussion.controller.exception;

import com.example.discussion.service.exception.EntityNotFoundException;
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

}
