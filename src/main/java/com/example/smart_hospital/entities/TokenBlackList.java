package com.example.smart_hospital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String token;
    @ManyToOne
    private Utente utente;
    @Column
    private LocalDateTime insertTime;
    @Column
    private LocalDateTime lastUpdate;
}
