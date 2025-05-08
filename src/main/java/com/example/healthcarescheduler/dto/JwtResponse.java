package com.example.healthcarescheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String userType; // "DOCTOR", "PATIENT", or "ADMIN"
    private Long profileId; // doctorId or patientId

    public JwtResponse(String token, Long id, String username, String email, List<String> roles, String userType, Long profileId) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.userType = userType;
        this.profileId = profileId;
    }
}