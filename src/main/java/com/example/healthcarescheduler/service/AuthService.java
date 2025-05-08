package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.*;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    UserDTO registerUser(RegisterRequest request);
    UserDTO registerDoctor(RegisterRequest request);
    UserDTO registerPatient(RegisterRequest request);
    boolean verifyEmail(String token);
    boolean requestPasswordReset(String email);
    boolean resetPassword(String token, String newPassword);
}