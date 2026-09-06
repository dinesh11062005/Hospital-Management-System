package com.hospital.management.repository;

import com.hospital.management.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByAvailability(String availability);

}