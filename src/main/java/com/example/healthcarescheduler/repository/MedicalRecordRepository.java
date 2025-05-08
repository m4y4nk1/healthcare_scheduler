package com.example.healthcarescheduler.repository;


import com.example.healthcarescheduler.model.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPatientId(Long patientId);

    Page<MedicalRecord> findByPatientId(Long patientId, Pageable pageable);

    List<MedicalRecord> findByDoctorId(Long doctorId);

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT m FROM MedicalRecord m WHERE m.doctor.id = :doctorId AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId AND m.followUpNeeded = true AND m.followUpDate > :currentDate")
    List<MedicalRecord> findPendingFollowUps(@Param("patientId") Long patientId, @Param("currentDate") LocalDateTime currentDate);
}