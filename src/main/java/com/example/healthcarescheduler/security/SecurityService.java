package com.example.healthcarescheduler.security;

import com.example.healthcarescheduler.model.Appointment;
import com.example.healthcarescheduler.model.User;
import com.example.healthcarescheduler.repository.AppointmentRepository;
import com.example.healthcarescheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityService")  // SpEL relies on this name
public class SecurityService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean canAccessAppointment(Long appointmentId) {
        User user = getCurrentUser();
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (user == null || appointment == null) return false;

        return appointment.getDoctor().getUser().getId().equals(user.getId()) ||
                appointment.getPatient().getUser().getId().equals(user.getId());
    }

    public boolean canModifyAppointment(Long appointmentId) {
        return canAccessAppointment(appointmentId);
    }

    public boolean isDoctorWithId(Long doctorId) {
        User user = getCurrentUser();
        return user != null && user.getDoctor() != null && user.getDoctor().getId().equals(doctorId);
    }

    public boolean isPatientWithId(Long patientId) {
        User user = getCurrentUser();
        return user != null && user.getPatient() != null && user.getPatient().getId().equals(patientId);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
