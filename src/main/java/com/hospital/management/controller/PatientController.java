package com.hospital.management.controller;

import com.hospital.management.model.Patient;
import com.hospital.management.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patient-list";
    }

    @GetMapping("/new")
    public String showPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @PostMapping("/save")
    public String savePatient(@ModelAttribute("patient") Patient patient) {
        patientService.savePatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID: " + id));
        model.addAttribute("patient", patient);
        return "patient-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}
