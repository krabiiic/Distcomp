package com.dedov.distributed_computing.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
            super(message);
        }
}
