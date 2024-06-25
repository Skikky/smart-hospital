package com.example.smart_hospital.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PazienteRequest {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private Double saldo;
    private String email;
    private String password;
}
