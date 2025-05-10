package com.dc.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private final int errorCode;

    public ServiceException(String devMessage, int errorCode) {
        super(devMessage);
        this.errorCode = errorCode;
    }

}
