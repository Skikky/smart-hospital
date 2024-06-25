package com.example.smart_hospital.controllers;

import com.example.smart_hospital.requests.MedicoRequest;
import com.example.smart_hospital.responses.MedicoResponse;
import com.example.smart_hospital.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN","MEDICO","PAZIENTE"})
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;


    @GetMapping("/all")
    public List<MedicoResponse> getAllMedici() {
        return medicoService.getAllMedici();
    }

    @Secured({"ADMIN","MEDICO"})
    @GetMapping("/get/{id}")
    public ResponseEntity<MedicoResponse> getMedicoResponseById(@PathVariable Long id) {
        try {
            MedicoResponse medico = medicoService.getMedicoResponseById(id);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Secured({"ADMIN","MEDICO"})
    @PutMapping("/update/{id}")
    public ResponseEntity<MedicoResponse> updateMedico(@PathVariable Long id, @RequestBody MedicoRequest medicoRequest) {
        try {
            MedicoResponse medico = medicoService.updateMedico(id, medicoRequest);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Secured({"ADMIN","MEDICO"})
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
