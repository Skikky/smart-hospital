package com.example.smart_hospital.exceptions;

public class InvalidRoleException extends Exception{
    @Override
    public String getMessage() {
        return "Il ruolo inserito non è valido. Deve essere UTENTE o RISTORATORE.";
    }
}
