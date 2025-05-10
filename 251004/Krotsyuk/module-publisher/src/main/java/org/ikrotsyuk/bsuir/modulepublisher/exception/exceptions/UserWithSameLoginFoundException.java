package org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions;

public class UserWithSameLoginFoundException extends RuntimeException {
    public UserWithSameLoginFoundException(String message){
        super(message);
    }
}
