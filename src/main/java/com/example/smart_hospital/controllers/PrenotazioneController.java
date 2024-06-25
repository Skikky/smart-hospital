package com.example.smart_hospital.controllers;

import com.example.smart_hospital.entities.Prenotazione;
import com.example.smart_hospital.requests.PrenotazioneRequest;
import com.example.smart_hospital.responses.PrenotazioneResponse;
import com.example.smart_hospital.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN","MEDICO","PAZIENTE"})
@RequestMapping("/prenotazione")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;

    @PostMapping("/prenota-visita")
    public ResponseEntity<PrenotazioneResponse> prenotaVisita(@RequestBody PrenotazioneRequest prenotazioneRequest) {
        PrenotazioneResponse prenotazione = prenotazioneService.prenotaVisita(prenotazioneRequest);
        return ResponseEntity.ok(prenotazione);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PrenotazioneResponse> getPrenotazioneById(@PathVariable Long id) {
        PrenotazioneResponse prenotazione = prenotazioneService.getPrenotazioneResponseById(id);
        return prenotazione != null ? ResponseEntity.ok(prenotazione) : ResponseEntity.notFound().build();
    }

    @Secured({"ADMIN"})
    @GetMapping("/all")
    public List<PrenotazioneResponse> getAllPrenotazioni() {
        return prenotazioneService.getAllPrenotazioni();
    }

    @Secured({"ADMIN","PAZIENTE"})
    @GetMapping("/all/{idPaziente}")
    public ResponseEntity<List<PrenotazioneResponse>> getPrenotazioniByPaziente(@PathVariable Long idPaziente) {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.getPrenotazioniByPaziente(idPaziente);
        return ResponseEntity.ok(prenotazioni);
    }

    @Secured({"ADMIN","PAZIENTE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        prenotazioneService.deletePrenotazione(id);
        return ResponseEntity.noContent().build();
    }
}
