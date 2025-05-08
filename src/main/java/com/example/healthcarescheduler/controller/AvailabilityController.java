package com.example.healthcarescheduler.controller;

import com.example.healthcarescheduler.dto.AvailabilitySlotDTO;
import com.example.healthcarescheduler.service.AvailabilitySlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AvailabilityController {

    @Autowired
    private AvailabilitySlotService availabilitySlotService;

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') and @securityService.isDoctorWithId(#doctorId)")
    public ResponseEntity<List<AvailabilitySlotDTO>> getSlotsForDoctor(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(availabilitySlotService.getSlotsForDoctorAndDate(doctorId, date));
    }

    @PostMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR') and @securityService.isDoctorWithId(#doctorId)")
    public ResponseEntity<List<AvailabilitySlotDTO>> createSlots(
            @PathVariable Long doctorId,
            @Valid @RequestBody List<AvailabilitySlotDTO> slots) {
        return ResponseEntity.ok(availabilitySlotService.createAvailabilitySlots(doctorId, slots));
    }
}
