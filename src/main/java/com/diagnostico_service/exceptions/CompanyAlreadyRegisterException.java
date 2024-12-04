package com.diagnostico_service.exceptions;

public class CompanyAlreadyRegisterException extends RuntimeException {

    public CompanyAlreadyRegisterException(String message){
        super(message);
    }
}
