package com.example.smart_hospital.exceptions;

public class PasswordUgualiException extends Exception{
    @Override
    public String getMessage() {
        return "La nuova password non può essere uguale a quella vecchia ";
    }
}
