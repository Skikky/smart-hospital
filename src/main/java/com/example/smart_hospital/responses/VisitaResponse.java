package com.example.smart_hospital.responses;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VisitaResponse {
    private Long id;
    private LocalDateTime inizioDisponibilita;
    private LocalDateTime fineDisponibilita;
    private Double prezzo;
    private Long idPaziente;
    private Long idMedico;
    private Boolean isTerminata;
    private int durata;
}
