package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.MedicalRecordDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Override
    public MedicalRecordDTO getRecordByAppointmentId(Long appointmentId) {
        // TODO: Implement logic
        return null;
    }

    @Override
    public List<MedicalRecordDTO> getRecordsByPatientId(Long patientId) {
        // TODO: Implement logic
        return null;
    }

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        // TODO: Implement logic
        return null;
    }
}