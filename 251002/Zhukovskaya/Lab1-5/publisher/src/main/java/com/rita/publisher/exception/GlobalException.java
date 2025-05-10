package com.rita.publisher.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private String message;
    private HttpStatus status;
    private String postfix;
}
