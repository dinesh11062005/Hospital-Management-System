package com.hospital.management.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hospital.management.model.Appointment;
import com.hospital.management.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final Collection<String> activeStatuses =
            Arrays.asList("Pending", "Confirmed");

    public AppointmentService(
            AppointmentRepository appointmentRepository) {

        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {

        return appointmentRepository.findById(id);
    }

    public Appointment saveAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {

        appointmentRepository.deleteById(id);
    }

    public boolean isPatientAlreadyAppointed(
            Long patientId,
            LocalDate appointmentDate,
            Long appointmentId) {

        if (appointmentId == null) {

            return appointmentRepository
                    .existsByPatient_IdAndAppointmentDateAndStatusIn(
                            patientId,
                            appointmentDate,
                            activeStatuses);
        }

        return appointmentRepository
                .existsByPatient_IdAndAppointmentDateAndStatusInAndIdNot(
                        patientId,
                        appointmentDate,
                        activeStatuses,
                        appointmentId);
    }

    public boolean isDoctorAlreadyBooked(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Long appointmentId) {

        if (appointmentId == null) {

            return appointmentRepository
                    .existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                            doctorId,
                            appointmentDate,
                            appointmentTime,
                            activeStatuses);
        }

        return appointmentRepository
                .existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndStatusInAndIdNot(
                        doctorId,
                        appointmentDate,
                        appointmentTime,
                        activeStatuses,
                        appointmentId);
    }
}