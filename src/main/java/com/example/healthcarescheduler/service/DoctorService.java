package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.DoctorDTO;
import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO getDoctorById(Long id);
    DoctorDTO updateDoctor(DoctorDTO dto);
}
