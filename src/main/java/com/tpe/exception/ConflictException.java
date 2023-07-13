package com.tpe.exception;

public class ConflictException extends RuntimeException{

    public ConflictException(String message) {  //RunTimeException clasinin tek String parametreli constructor'ini olusturup icine methodum icinde mesajimi yazcam
        super(message);
    }
}
