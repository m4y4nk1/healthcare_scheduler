package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.MedicalRecordDTO;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecordDTO getRecordByAppointmentId(Long appointmentId);
    List<MedicalRecordDTO> getRecordsByPatientId(Long patientId);
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto);
}
