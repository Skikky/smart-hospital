package com.example.smart_hospital.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicoRequest {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String specializzazione;
}
