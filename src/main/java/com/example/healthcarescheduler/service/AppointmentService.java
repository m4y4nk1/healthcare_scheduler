package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.AppointmentDTO;
import com.example.healthcarescheduler.model.Appointment.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Page<AppointmentDTO> getAllAppointments(Pageable pageable);
    AppointmentDTO getAppointmentById(Long id);
    AppointmentDTO createAppointment(AppointmentDTO dto);
    AppointmentDTO updateAppointment(AppointmentDTO dto);
    void cancelAppointment(Long id);
    List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId);
    List<AppointmentDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);
    List<AppointmentDTO> getAppointmentsByPatient(Long patientId);
    AppointmentDTO updateAppointmentStatus(Long id, AppointmentStatus status);
    List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date);
}
