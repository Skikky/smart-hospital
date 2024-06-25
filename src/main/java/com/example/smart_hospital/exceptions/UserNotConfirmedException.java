package com.example.smart_hospital.exceptions;

public class UserNotConfirmedException extends Exception {
    @Override
    public String getMessage() {
        return "devi confermare l'account per poter accedere ";
    }
}
