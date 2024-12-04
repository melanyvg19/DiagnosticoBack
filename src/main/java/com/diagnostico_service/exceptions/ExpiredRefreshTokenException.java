package com.diagnostico_service.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException{

    public ExpiredRefreshTokenException(String message){
        super(message);
    }

}
