package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.requests.VisitaRequest;
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
    public List<Visita> getAllVisite() {
        return visitaService.getAllVisite();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Visita> getVisitaById(@PathVariable Long id) {
        try {
            Visita visita = visitaService.getVisitaById(id);
            return ResponseEntity.ok(visita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public Visita createVisita(@RequestBody VisitaRequest visitaRequest) {
        return visitaService.createVisita(visitaRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Visita> updateVisita(@PathVariable Long id, @RequestBody VisitaRequest visitaRequest) {
        try {
            Visita visita = visitaService.updateVisita(id, visitaRequest);
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
}
