// AuthController.java
package com.example.healthcarescheduler.controller;

import com.example.healthcarescheduler.dto.*;
import com.example.healthcarescheduler.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.authenticateUser(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        return new ResponseEntity<>(authService.registerUser(req), HttpStatus.CREATED);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody RegisterRequest req) {
        return new ResponseEntity<>(authService.registerDoctor(req), HttpStatus.CREATED);
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody RegisterRequest req) {
        return new ResponseEntity<>(authService.registerPatient(req), HttpStatus.CREATED);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable String token) {
        return authService.verifyEmail(token) ? ResponseEntity.ok("Verified") : ResponseEntity.badRequest().body("Invalid Token");
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        return authService.requestPasswordReset(email) ? ResponseEntity.ok("Reset email sent") : ResponseEntity.badRequest().body("Failed");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<?> confirmReset(@RequestParam String token, @RequestParam String newPassword) {
        return authService.resetPassword(token, newPassword) ? ResponseEntity.ok("Password reset") : ResponseEntity.badRequest().body("Invalid token");
    }
}