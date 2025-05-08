package com.example.healthcarescheduler.repository;

import com.example.healthcarescheduler.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserId(Long userId);

    @Query("SELECT p FROM Patient p WHERE p.user.firstName LIKE %:name% OR p.user.lastName LIKE %:name%")
    List<Patient> findByName(@Param("name") String name);

    @Query("SELECT p FROM Patient p WHERE p.insuranceProvider = :provider")
    List<Patient> findByInsuranceProvider(@Param("provider") String provider);

    @Query("SELECT p FROM Patient p JOIN p.appointments a WHERE a.doctor.id = :doctorId")
    List<Patient> findByDoctorId(@Param("doctorId") Long doctorId);
}