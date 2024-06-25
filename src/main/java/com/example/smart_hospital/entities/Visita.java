package com.example.smart_hospital.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Table(name = "visita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Check(constraints = "fine_disponibilita > inizio_disponibilita")
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime inizioDisponibilita;

    @Column(nullable = false)
    private LocalDateTime fineDisponibilita;
    @Column(nullable = false)
    private Double prezzo;

    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Utente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Utente medico;
    @Column
    private String referto;
    @Column(nullable = false)
    private Boolean isTerminata;
    @Column(nullable = false)
    private int durata;
}

