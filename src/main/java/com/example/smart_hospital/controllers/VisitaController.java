package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.requests.VisitaRequest;
import com.example.smart_hospital.responses.VisitaResponse;
import com.example.smart_hospital.services.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visita")
public class VisitaController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping("/all")
    public List<VisitaResponse> getAllVisite() {
        return visitaService.getAllVisite();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<VisitaResponse> getVisitaById(@PathVariable Long id) {
        try {
            VisitaResponse visita = visitaService.getVisitaResponseById(id);
            return ResponseEntity.ok(visita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public VisitaResponse createVisita(@RequestBody VisitaRequest visitaRequest) {
        return visitaService.createVisita(visitaRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VisitaResponse> updateVisita(@PathVariable Long id, @RequestBody VisitaRequest visitaRequest) {
        try {
            VisitaResponse visita = visitaService.updateVisita(id, visitaRequest);
            return ResponseEntity.ok(visita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVisita(@PathVariable Long id) {
        try {
            visitaService.deleteVisitaById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/medico/{idMedico}")
    public List<VisitaResponse> trovaVisitePerMedico(@PathVariable Long idMedico) {
        Utente medico = new Utente();
        medico.setId(idMedico);
        return visitaService.trovaVisitePerMedico(medico);
    }

    @GetMapping("/disponibili")
    public List<VisitaResponse> trovaVisiteDisponibili() {
        return visitaService.trovaVisiteDisponibili();
    }

    @GetMapping("/paziente/{idPaziente}")
    public List<VisitaResponse> trovaVisitePerPaziente(@PathVariable Long idPaziente) {
        Utente paziente = new Utente();
        paziente.setId(idPaziente);
        return visitaService.trovaVisitePerPaziente(paziente);
    }
}
