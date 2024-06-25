package com.example.smart_hospital.requests;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrenotazioneRequest {
    private Long idPaziente;
    private Long idVisita;
    private LocalDateTime dataPrenotazione;
}
