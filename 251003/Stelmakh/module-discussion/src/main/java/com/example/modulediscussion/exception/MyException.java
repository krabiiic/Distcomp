package com.example.modulediscussion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyException extends RuntimeException{
    public MyException(String s){super(s);}
}

