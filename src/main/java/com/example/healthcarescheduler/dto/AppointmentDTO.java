package com.example.healthcarescheduler.dto;

import com.example.healthcarescheduler.model.Appointment.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private String notes;
}
