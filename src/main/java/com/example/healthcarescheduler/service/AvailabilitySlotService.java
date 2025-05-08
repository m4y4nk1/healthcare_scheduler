package com.example.healthcarescheduler.service;

import com.example.healthcarescheduler.dto.AvailabilitySlotDTO;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilitySlotService {
    List<AvailabilitySlotDTO> getSlotsForDoctorAndDate(Long doctorId, LocalDate date);
    List<AvailabilitySlotDTO> createAvailabilitySlots(Long doctorId, List<AvailabilitySlotDTO> slots);
}
