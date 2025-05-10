package com.example.restservice.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseTo {
    private final String errorMessage;
    private final String errorCode;

    public ErrorResponseTo(String errorMessage, HttpStatus status, int customCode) {
        this.errorMessage = errorMessage;
        this.errorCode = generateErrorCode(status, customCode);
    }

    private String generateErrorCode(HttpStatus status, int customCode) {
        return String.format("%d%02d", status.value(), customCode);
    }
}