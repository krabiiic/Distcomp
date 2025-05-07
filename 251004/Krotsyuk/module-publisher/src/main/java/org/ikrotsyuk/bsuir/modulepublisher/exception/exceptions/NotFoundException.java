package org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("not found exception text");
    }
}
