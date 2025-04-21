package com.example.restservice.controller;

import com.example.restservice.dto.response.ErrorResponseTo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseTo> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponseTo errorResponse = new ErrorResponseTo(ex.getMessage(), HttpStatus.BAD_REQUEST, 20);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseTo> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponseTo errorResponse = new ErrorResponseTo(ex.getMessage(), HttpStatus.FORBIDDEN, 30);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponseTo> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponseTo errorResponse = new ErrorResponseTo(ex.getMessage(), HttpStatus.NOT_FOUND, 40);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseTo> handleGeneralException(Exception ex) {
        ErrorResponseTo errorResponse = new ErrorResponseTo(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, 50);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
