package com.example.smart_hospital.requests;

import com.example.smart_hospital.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private Role desiredRole;
    private String registrationToken;
}
