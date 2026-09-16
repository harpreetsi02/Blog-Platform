package com.backend.blogplatform.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
