package com.example.smart_hospital.responses;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrenotazioneResponse {
    private Long id;
    private Long idPaziente;
    private Long idVisita;
    private LocalDateTime dataPrenotazione;
}
