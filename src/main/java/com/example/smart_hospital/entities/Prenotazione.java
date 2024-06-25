package com.example.smart_hospital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Utente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visita_id", nullable = false)
    private Visita visita;

    @Column(nullable = false)
    private LocalDateTime dataPrenotazione;
}
