package com.hospital.management.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.management.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByPatient_IdAndAppointmentDateAndStatusIn(
            Long patientId,
            LocalDate appointmentDate,
            Collection<String> statuses);

    boolean existsByPatient_IdAndAppointmentDateAndStatusInAndIdNot(
            Long patientId,
            LocalDate appointmentDate,
            Collection<String> statuses,
            Long id);

    boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatusIn(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Collection<String> statuses);

    boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatusInAndIdNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Collection<String> statuses,
            Long id);
}