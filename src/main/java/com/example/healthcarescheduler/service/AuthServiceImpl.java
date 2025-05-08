package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.*;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public UserDTO registerUser(RegisterRequest request) {
        return null;
    }

    @Override
    public UserDTO registerDoctor(RegisterRequest request) {
        return null;
    }

    @Override
    public UserDTO registerPatient(RegisterRequest request) {
        return null;
    }

    @Override
    public boolean verifyEmail(String token) {
        return false;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        return false;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        return false;
    }
}
