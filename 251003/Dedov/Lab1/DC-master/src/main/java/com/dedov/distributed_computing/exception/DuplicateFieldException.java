package com.dedov.distributed_computing.exception;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String fieldName, String fieldValue) {
        super(String.format("Value '%s' for field '%s' already exists.", fieldValue, fieldName));
    }
}
