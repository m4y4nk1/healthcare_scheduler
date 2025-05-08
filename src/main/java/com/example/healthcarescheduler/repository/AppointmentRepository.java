package com.example.healthcarescheduler.repository;

import com.example.healthcarescheduler.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.startTime >= :startDate AND a.endTime <= :endDate")
    List<Appointment> findByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.startTime >= :startDate AND a.endTime <= :endDate")
    List<Appointment> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = :status")
    List<Appointment> findByDoctorIdAndStatus(
            @Param("doctorId") Long doctorId,
            @Param("status") Appointment.AppointmentStatus status
    );

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
    List<Appointment> findByPatientIdAndStatus(
            @Param("patientId") Long patientId,
            @Param("status") Appointment.AppointmentStatus status
    );

    @Query("SELECT a FROM Appointment a WHERE a.startTime >= :now AND a.reminderSent = false AND a.status = 'CONFIRMED'")
    List<Appointment> findUpcomingAppointmentsForReminders(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Appointment a WHERE a.startTime >= :start AND a.endTime <= :end")
    Page<Appointment> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.startTime >= :now")
    List<Appointment> findUpcomingAppointmentsByStatus(
            @Param("status") Appointment.AppointmentStatus status,
            @Param("now") LocalDateTime now
    );

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.startTime >= :start AND a.endTime <= :end")
    Integer countAppointmentsByDoctorAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}