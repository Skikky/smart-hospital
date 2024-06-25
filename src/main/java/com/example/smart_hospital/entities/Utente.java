package com.example.smart_hospital.entities;

import com.example.smart_hospital.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Check(constraints = "(ruolo != 'MEDICO' OR saldo IS NULL) AND (ruolo != 'PAZIENTE' OR specializzazione IS NULL)")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false, unique = true)
    private String codiceFiscale;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role ruolo;

    @Column
    @Check(constraints = "saldo >= 0")
    private double saldo;

    @Column
    private String specializzazione;

    @OneToMany(mappedBy = "paziente")
    private List<Visita> visitePaziente;

    @OneToMany(mappedBy = "medico")
    private List<Visita> visiteMedico;
}
