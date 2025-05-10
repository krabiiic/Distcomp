package com.example.lab1.service.exception;

public class DuplicateLoginException extends RuntimeException {
    public DuplicateLoginException(String message) {

      super(message);
    }
}
