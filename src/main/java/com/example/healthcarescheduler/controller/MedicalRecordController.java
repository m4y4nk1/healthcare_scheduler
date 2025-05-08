package com.example.healthcarescheduler.controller;

import com.example.healthcarescheduler.dto.MedicalRecordDTO;
import com.example.healthcarescheduler.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.canAccessAppointment(#appointmentId)")
    public ResponseEntity<MedicalRecordDTO> getRecordByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(medicalRecordService.getRecordByAppointmentId(appointmentId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') and @securityService.isPatientWithId(#patientId)")
    public ResponseEntity<List<MedicalRecordDTO>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByPatientId(patientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordDTO> createRecord(@Valid @RequestBody MedicalRecordDTO dto) {
        return new ResponseEntity<>(medicalRecordService.createMedicalRecord(dto), HttpStatus.CREATED);
    }
}
