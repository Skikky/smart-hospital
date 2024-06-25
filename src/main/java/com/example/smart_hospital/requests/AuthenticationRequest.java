package com.example.smart_hospital.requests;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    private String email;
    private String password;
}
