package com.example.smart_hospital.responses;

import com.example.smart_hospital.enums.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PazienteResponse {
    private Long id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private Role role;
    private Double saldo;
    private String email;
    private String password;
}
