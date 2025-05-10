package com.example.rv1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExeptionForbidden extends RuntimeException{
    public ExeptionForbidden(String s){super(s);}
}
