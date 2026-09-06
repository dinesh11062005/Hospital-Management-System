package com.hospital.management.controller;

import com.hospital.management.model.Doctor;
import com.hospital.management.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String listDoctors(Model model) {

        model.addAttribute("doctors",
                doctorService.getAllDoctors());

        return "doctor-list";
    }

    @GetMapping("/new")
    public String showDoctorForm(Model model) {

        model.addAttribute("doctor", new Doctor());

        return "doctor-form";
    }

    @PostMapping("/save")
    public String saveDoctor(
            @ModelAttribute("doctor") Doctor doctor) {

        doctorService.saveDoctor(doctor);

        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editDoctor(
            @PathVariable Long id,
            Model model) {

        Doctor doctor = doctorService
                .getDoctorById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid doctor ID: " + id));

        model.addAttribute("doctor", doctor);

        return "doctor-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return "redirect:/doctors";
    }
}