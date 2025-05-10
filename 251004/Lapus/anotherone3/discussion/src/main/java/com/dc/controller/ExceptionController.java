package com.dc.controller;

import com.dc.exception.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<ExceptionObject> commonException2(@RequestBody ServiceException ex) {
        return ResponseEntity.status(ex.getErrorCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionObject(ex.getErrorCode(), ex.getMessage(), ex.getMessage()));
    }

    public record ExceptionObject(int code, String status, String message){}
}
