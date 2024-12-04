package com.diagnostico_service.exceptions;

public class BadUserCredentialsException extends RuntimeException{

    public BadUserCredentialsException(String message){
        super(message);
    }

}
