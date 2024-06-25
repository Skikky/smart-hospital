package com.example.smart_hospital.exceptions;

public class PasswordDeboleException extends Exception{
    @Override
    public String getMessage() {
        return "La password deve essere di 8 caratteri, avere 1 maiuscola, 1 numero e un carattere speciale";
    }
}
