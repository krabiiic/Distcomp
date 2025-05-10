package com.rita.discussion.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({EntityNotFoundException.class,  ExecutionException.class, InterruptedException.class, TimeoutException.class})
    public ResponseEntity<ResponseException> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage(),HttpStatus.BAD_REQUEST,"EF"), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseException> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage(),HttpStatus.BAD_REQUEST,"CV"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseException> handleGlobalException(GlobalException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage(),HttpStatus.FORBIDDEN,ex.getPostfix()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseException(errors.toString(), HttpStatus.BAD_REQUEST, "AV"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseException> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage(),HttpStatus.BAD_REQUEST,"NR"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ResponseException> handleTypeMismatchException(TypeMismatchException ex) {
        return new ResponseEntity<>(new ResponseException(ex.getMessage(),HttpStatus.BAD_REQUEST,"TM"), HttpStatus.BAD_REQUEST);
    }

    @Data
    public static class ResponseException {
        private String errorMessage;
        private String errorCode;
        public ResponseException(String errorMessage,HttpStatus status,String postfix){
            this.errorMessage=errorMessage;
            this.errorCode= String.valueOf(status.value())+postfix;
        }
    }
}
