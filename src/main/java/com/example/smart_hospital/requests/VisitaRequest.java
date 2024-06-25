package com.example.smart_hospital.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VisitaRequest {
    private LocalDateTime dataOra;
    private double prezzo;
    private Long idPaziente;
    private Long idMedico;
    private Long idReferto;
}
