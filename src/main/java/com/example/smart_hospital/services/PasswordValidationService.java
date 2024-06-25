package com.example.smart_hospital.services;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PasswordValidationService {

    // Regex pattern: at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }
}
