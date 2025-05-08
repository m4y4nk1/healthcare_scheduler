package com.example.healthcarescheduler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "is_followup")
    private boolean isFollowup = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_appointment_id")
    private Appointment previousAppointment;

    @Column(name = "notes_for_doctor", length = 1000)
    private String notesForDoctor;

    @Column(name = "doctor_notes", length = 1000)
    private String doctorNotes;

    @Column(name = "appointment_type")
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType = AppointmentType.IN_PERSON;

    @Column(name = "virtual_meeting_link")
    private String virtualMeetingLink;

    @Column(name = "reminder_sent")
    private boolean reminderSent = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppointmentStatus {
        SCHEDULED,
        CONFIRMED,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }

    public enum AppointmentType {
        IN_PERSON,
        VIRTUAL,
        PHONE_CALL
    }
}