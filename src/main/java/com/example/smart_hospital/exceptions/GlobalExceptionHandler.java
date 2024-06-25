package com.example.smart_hospital.exceptions;

import com.example.smart_hospital.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse("Illegal Argument Exception", "Id non presente nella tabella");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("Entity not found Exception", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordDeboleException.class)
    public ResponseEntity<ErrorResponse> handlePasswordDeboleException(PasswordDeboleException e) {
        ErrorResponse errorResponse = new ErrorResponse("Password Debole Exception", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordUgualiException.class)
    public ResponseEntity<ErrorResponse> handlePasswordUgualiException(PasswordUgualiException e) {
        ErrorResponse errorResponse = new ErrorResponse("Password Uguali Exception", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordSbagliataException.class)
    public ResponseEntity<ErrorResponse> handlePasswordSbagliataException(PasswordSbagliataException e) {
        ErrorResponse errorResponse = new ErrorResponse("Password Sbagliata Exception", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
