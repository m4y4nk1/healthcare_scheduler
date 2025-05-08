package com.example.healthcarescheduler.repository;

import com.example.healthcarescheduler.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctorId(Long doctorId);

    List<AvailabilitySlot> findByDoctorIdAndIsRecurring(Long doctorId, boolean isRecurring);

    List<AvailabilitySlot> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    @Query("SELECT a FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId AND a.isRecurring = false " +
            "AND a.specificStartDateTime >= :startDate AND a.specificEndDateTime <= :endDate AND a.isAvailable = true")
    List<AvailabilitySlot> findAvailableSpecificSlotsByDoctorAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId AND a.isRecurring = true " +
            "AND a.dayOfWeek = :dayOfWeek AND a.isAvailable = true")
    List<AvailabilitySlot> findAvailableRecurringSlotsByDoctorAndDayOfWeek(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek
    );

    @Query("SELECT a FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId AND " +
            "((a.isRecurring = false AND a.specificStartDateTime >= :startDate AND a.specificEndDateTime <= :endDate) OR " +
            "(a.isRecurring = true AND a.dayOfWeek IN :daysOfWeek))")
    List<AvailabilitySlot> findAllSlotsByDoctorAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("daysOfWeek") List<DayOfWeek> daysOfWeek
    );
}