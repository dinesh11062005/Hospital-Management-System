package com.hospital.management.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.management.model.Appointment;
import com.hospital.management.model.Doctor;
import com.hospital.management.model.Patient;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.service.AppointmentService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public String listAppointments(Model model) {

        model.addAttribute(
                "appointments",
                appointmentService.getAllAppointments());

        return "appointment-list";
    }

    @GetMapping("/new")
    public String showAppointmentForm(Model model) {

        Appointment appointment = new Appointment();

        model.addAttribute(
                "appointment",
                appointment);

        model.addAttribute(
                "patients",
                patientRepository.findAll());

        model.addAttribute(
                "doctors",
                doctorRepository.findByAvailability("Available"));

        model.addAttribute(
                "allAppointments",
                appointmentService.getAllAppointments());

        return "appointment-form";
    }

    @PostMapping("/save")
    public String saveAppointment(
            @ModelAttribute("appointment")
            Appointment appointment,
            Model model) {

        Long appointmentId = appointment.getId();

        /*
         * ================= PATIENT CHECK =================
         */

        if (appointment.getPatient() != null
                && appointment.getAppointmentDate() != null) {

            boolean patientAlreadyBooked =
                    appointmentService.isPatientAlreadyAppointed(
                            appointment.getPatient().getId(),
                            appointment.getAppointmentDate(),
                            appointmentId);

            if (patientAlreadyBooked) {

                prepareFormData(model, appointment);

                model.addAttribute(
                        "errorMessage",
                        "This patient already has an active appointment on "
                                + appointment.getAppointmentDate()
                                + ".");

                return "appointment-form";
            }
        }

        /*
         * ================= DOCTOR CHECK =================
         */

        if (appointment.getDoctor() != null
                && appointment.getAppointmentDate() != null
                && appointment.getAppointmentTime() != null) {

            boolean doctorAlreadyBooked =
                    appointmentService.isDoctorAlreadyBooked(
                            appointment.getDoctor().getId(),
                            appointment.getAppointmentDate(),
                            appointment.getAppointmentTime(),
                            appointmentId);

            if (doctorAlreadyBooked) {

                prepareFormData(model, appointment);

                model.addAttribute(
                        "errorMessage",
                        "This doctor is already booked at the selected date and time.");

                return "appointment-form";
            }
        }

        /*
         * ================= SAVE =================
         */

        appointmentService.saveAppointment(appointment);

        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String editAppointment(
            @PathVariable Long id,
            Model model) {

        Appointment appointment =
                appointmentService
                .getAppointmentById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid appointment ID: " + id));

        prepareFormData(model, appointment);

        return "appointment-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return "redirect:/appointments";
    }

    /*
     * =====================================================
     * PREPARE FORM DATA
     * =====================================================
     */

    private void prepareFormData(
            Model model,
            Appointment appointment) {

        model.addAttribute(
                "appointment",
                appointment);

        /*
         * ================= PATIENTS =================
         *
         * We send all patients to the page.
         *
         * JavaScript will hide patients who already
         * have an active appointment on the selected date.
         */

        model.addAttribute(
                "patients",
                patientRepository.findAll());

        /*
         * ================= DOCTORS =================
         *
         * Only Available doctors are shown.
         *
         * Current doctor is also kept during editing.
         */

        List<Doctor> availableDoctors =
                new ArrayList<>(
                        doctorRepository
                        .findByAvailability("Available"));

        if (appointment.getDoctor() != null) {

            boolean currentDoctorExists =
                    availableDoctors
                    .stream()
                    .anyMatch(doctor ->
                            doctor.getId()
                            .equals(
                                    appointment
                                    .getDoctor()
                                    .getId()));

            if (!currentDoctorExists) {

                availableDoctors.add(
                        appointment.getDoctor());
            }
        }

        model.addAttribute(
                "doctors",
                availableDoctors);

        /*
         * Send existing appointments to HTML.
         * JavaScript uses this to control
         * patient and doctor availability.
         */

        model.addAttribute(
                "allAppointments",
                appointmentService.getAllAppointments());
    }
}