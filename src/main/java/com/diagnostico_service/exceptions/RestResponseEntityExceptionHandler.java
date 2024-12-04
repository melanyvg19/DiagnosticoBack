package com.diagnostico_service.exceptions;

import com.diagnostico_service.exceptions.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorMessage> objectNotFoundException(ObjectNotFoundException exception){
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @ExceptionHandler(BadUserCredentialsException.class)
    public ResponseEntity<ErrorMessage> userNotRegisterException(BadUserCredentialsException exception){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ErrorMessage> expiredRefreshTokenExceptionHandler(ExpiredRefreshTokenException exception){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    @ExceptionHandler(CompanyAlreadyRegisterException.class)
    public ResponseEntity<ErrorMessage> companyAlreadyRegisterExceptionHandler(CompanyAlreadyRegisterException exception){
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.OK.value(), HttpStatus.OK,exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }
}
