package com.example.smart_hospital.exceptions;

public class PasswordSbagliataException extends Exception{
    @Override
    public String getMessage() {
        return "Hai sbagliato ad inserire la password";
    }
}
