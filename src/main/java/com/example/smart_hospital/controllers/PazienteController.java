package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.requests.PazienteRequest;
import com.example.smart_hospital.services.PazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paziente")
public class PazienteController {

    @Autowired
    private PazienteService pazienteService;

    @GetMapping("/all")
    public List<Utente> getAllPazienti() {
        return pazienteService.getAllPazienti();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Utente> getPazienteById(@PathVariable Long id) {
        try {
            Utente paziente = pazienteService.getPazienteById(id);
            return ResponseEntity.ok(paziente);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public Utente createPaziente(@RequestBody PazienteRequest pazienteRequest) {
        return pazienteService.createPaziente(pazienteRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Utente> updatePaziente(@PathVariable Long id, @RequestBody PazienteRequest pazienteRequest) {
        try {
            Utente paziente = pazienteService.updatePaziente(id, pazienteRequest);
            return ResponseEntity.ok(paziente);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable Long id) {
        try {
            pazienteService.deletePazienteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

