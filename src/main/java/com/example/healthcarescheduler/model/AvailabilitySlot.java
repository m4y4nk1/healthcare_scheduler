package com.example.healthcarescheduler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "availability_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // For recurring slots (weekly schedule)
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    // For specific date-time slots (one-time availability)
    @Column(name = "specific_start_datetime")
    private LocalDateTime specificStartDateTime;

    @Column(name = "specific_end_datetime")
    private LocalDateTime specificEndDateTime;

    @Column(name = "is_recurring")
    private boolean isRecurring = true;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    // This helps to identify if this is a special case (like vacation)
    @Column(name = "slot_type")
    @Enumerated(EnumType.STRING)
    private SlotType slotType = SlotType.REGULAR;

    public enum SlotType {
        REGULAR,
        LUNCH_BREAK,
        VACATION,
        SICK_LEAVE,
        PERSONAL_TIME
    }
}