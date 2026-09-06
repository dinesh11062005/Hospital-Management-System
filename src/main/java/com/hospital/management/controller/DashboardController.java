package com.hospital.management.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hospital.management.model.Appointment;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

@Controller
public class DashboardController {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DashboardController(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        // ===============================
        // BASIC COUNTS
        // ===============================

        long totalDoctors = doctorRepository.count();

        long totalPatients = patientRepository.count();

        List<Appointment> appointments =
                appointmentRepository.findAll();

        long totalAppointments = appointments.size();


        // ===============================
        // STATUS COUNTS
        // ===============================

        long pendingAppointments =
                appointments.stream()
                        .filter(a -> "Pending".equals(a.getStatus()))
                        .count();

        long confirmedAppointments =
                appointments.stream()
                        .filter(a -> "Confirmed".equals(a.getStatus()))
                        .count();

        long completedAppointments =
                appointments.stream()
                        .filter(a -> "Completed".equals(a.getStatus()))
                        .count();

        long cancelledAppointments =
                appointments.stream()
                        .filter(a -> "Cancelled".equals(a.getStatus()))
                        .count();


        // ===============================
        // TODAY'S APPOINTMENTS
        // ===============================

        LocalDate today = LocalDate.now();

        List<Appointment> todayAppointments =
                appointments.stream()
                        .filter(a -> a.getAppointmentDate() != null)
                        .filter(a -> today.equals(a.getAppointmentDate()))
                        .sorted((a1, a2) -> {

                            if (a1.getAppointmentTime() == null) {
                                return 1;
                            }

                            if (a2.getAppointmentTime() == null) {
                                return -1;
                            }

                            return a1.getAppointmentTime()
                                    .compareTo(a2.getAppointmentTime());
                        })
                        .collect(Collectors.toList());


        // ===============================
        // RECENT APPOINTMENTS
        // ===============================

        List<Appointment> recentAppointments =
                appointments.stream()
                        .sorted((a1, a2) -> {

                            if (a1.getId() == null) {
                                return 1;
                            }

                            if (a2.getId() == null) {
                                return -1;
                            }

                            return a2.getId()
                                    .compareTo(a1.getId());
                        })
                        .limit(5)
                        .collect(Collectors.toList());


        // ===============================
        // SEND DATA TO HTML
        // ===============================

        model.addAttribute(
                "totalDoctors",
                totalDoctors);

        model.addAttribute(
                "totalPatients",
                totalPatients);

        model.addAttribute(
                "totalAppointments",
                totalAppointments);

        model.addAttribute(
                "pendingAppointments",
                pendingAppointments);

        model.addAttribute(
                "confirmedAppointments",
                confirmedAppointments);

        model.addAttribute(
                "completedAppointments",
                completedAppointments);

        model.addAttribute(
                "cancelledAppointments",
                cancelledAppointments);

        model.addAttribute(
                "todayAppointments",
                todayAppointments);

        model.addAttribute(
                "recentAppointments",
                recentAppointments);


        return "dashboard";
    }
}