package com.example.smart_hospital.entities;

import com.example.smart_hospital.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Check(constraints = "(role != 'MEDICO' OR saldo IS NULL) AND (role != 'PAZIENTE' OR specializzazione IS NULL)")
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
    private Role role;

    @Column
    @Check(constraints = "saldo >= 0")
    private Double saldo;

    @Column
    private String specializzazione;
}
