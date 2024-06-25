package com.example.smart_hospital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataOra;
    private double prezzo;

    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Utente paziente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Utente medico;

    // aggiungi il file per il referto
}

