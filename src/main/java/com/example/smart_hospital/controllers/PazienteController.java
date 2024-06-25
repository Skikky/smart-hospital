package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.requests.PazienteRequest;
import com.example.smart_hospital.responses.MedicoResponse;
import com.example.smart_hospital.responses.PazienteResponse;
import com.example.smart_hospital.services.PazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN","MEDICO","PAZIENTE"})
@RequestMapping("/paziente")
public class PazienteController {

    @Autowired
    private PazienteService pazienteService;

    @GetMapping("/all")
    public List<PazienteResponse> getAllPazienti() {
        return pazienteService.getAllPazienti();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PazienteResponse> getPazienteResponseById(@PathVariable Long id) {
        try {
            PazienteResponse paziente = pazienteService.getPazienteResponseById(id);
            return ResponseEntity.ok(paziente);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Secured({"ADMIN","PAZIENTE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<PazienteResponse> updatePaziente(@PathVariable Long id, @RequestBody PazienteRequest pazienteRequest) {
        try {
            PazienteResponse paziente = pazienteService.updatePaziente(id, pazienteRequest);
            return ResponseEntity.ok(paziente);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Secured({"ADMIN","PAZIENTE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaziente(@PathVariable Long id) {
        try {
            pazienteService.deletePazienteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find-specialisti/{specializzazione}")
    public List<MedicoResponse> findSpecialisti(@PathVariable String specializzazione) {
        return pazienteService.findSpecialisti(specializzazione);
    }
}

