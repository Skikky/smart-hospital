package com.example.smart_hospital.requests;

import com.example.smart_hospital.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Double saldo;
    private Role desiredRole;
    private String codiceFiscale;
    private String specializzazione;
}
