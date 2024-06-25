package com.example.smart_hospital.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
}
