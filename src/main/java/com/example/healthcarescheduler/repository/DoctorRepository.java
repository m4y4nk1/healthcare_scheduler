package com.example.healthcarescheduler.repository;

import com.example.healthcarescheduler.model.Doctor;
import com.example.healthcarescheduler.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    @Query("SELECT d FROM Doctor d WHERE d.user.firstName LIKE %:name% OR d.user.lastName LIKE %:name%")
    List<Doctor> findByName(@Param("name") String name);

    @Query("SELECT d FROM Doctor d JOIN d.specialties s WHERE s.id = :specialtyId")
    Page<Doctor> findBySpecialtyId(@Param("specialtyId") Long specialtyId, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.averageRating >= :minRating")
    List<Doctor> findByMinimumRating(@Param("minRating") Double minRating);

    @Query("SELECT d FROM Doctor d JOIN d.specialties s WHERE s.name = :specialtyName")
    List<Doctor> findBySpecialtyName(@Param("specialtyName") String specialtyName);

    boolean existsByLicenseNumber(String licenseNumber);

    @Query("SELECT d FROM Doctor d WHERE d.user.isEnabled = true")
    List<Doctor> findAllActive();
}