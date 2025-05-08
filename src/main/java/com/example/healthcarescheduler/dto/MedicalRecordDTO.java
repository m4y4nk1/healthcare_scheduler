package com.example.healthcarescheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {
    private Long id;
    private Long appointmentId;
    private String diagnosis;
    private String treatment;
    private String notes;
}
