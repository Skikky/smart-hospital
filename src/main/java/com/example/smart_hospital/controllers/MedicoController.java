package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.requests.MedicoRequest;
import com.example.smart_hospital.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/all")
    public List<Utente> getAllMedici() {
        return medicoService.getAllMedici();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Utente> getMedicoById(@PathVariable Long id) {
        try {
            Utente medico = medicoService.getMedicoById(id);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public Utente createMedico(@RequestBody MedicoRequest medicoRequest) {
        return medicoService.createMedico(medicoRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Utente> updateMedico(@PathVariable Long id, @RequestBody MedicoRequest medicoRequest) {
        try {
            Utente medico = medicoService.updateMedico(id, medicoRequest);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        try {
            medicoService.deleteMedicoById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
