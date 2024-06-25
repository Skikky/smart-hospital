package com.example.smart_hospital.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrenotaVisitaRequest {
    private LocalDateTime dataOra;
    private double prezzo;
    private Long idMedico;
}
