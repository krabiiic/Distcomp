package com.example.publisher.service.exception;

public class DuplicateLoginException extends RuntimeException {
    public DuplicateLoginException(String message) {

      super(message);
    }
}
