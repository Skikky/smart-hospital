package com.example.smart_hospital.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VisitaRequest {
    private LocalDateTime inizioDisponibilita;
    private LocalDateTime fineDisponibilita;
    private Double prezzo;
    private Long idMedico;
}
