package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.PatientDTO;
import java.util.List;

public interface PatientService {
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(Long id);
    PatientDTO updatePatient(PatientDTO dto);
}
