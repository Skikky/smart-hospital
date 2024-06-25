package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.requests.PazienteRequest;
import com.example.smart_hospital.requests.PrenotaVisitaRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PazienteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private VisitaService visitaService;

    public List<Utente> getAllPazienti() {
        return utenteRepository.findByRuolo(Role.PAZIENTE);
    }

    public Utente getPazienteById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRuolo() != Role.PAZIENTE) {
            throw new IllegalStateException("L'utente con id " + id + " non è un paziente");
        }

        return utente;
    }

    public Utente getMedicoById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRuolo() != Role.MEDICO) {
            throw new IllegalStateException("L'utente con id " + id + " non è un medico");
        }

        return utente;
    }

    public Utente createPaziente(PazienteRequest pazienteRequest) {
        Utente utente = Utente.builder()
                .nome(pazienteRequest.getNome())
                .cognome(pazienteRequest.getCognome())
                .codiceFiscale(pazienteRequest.getCodiceFiscale())
                .saldo(pazienteRequest.getSaldo())
                .ruolo(Role.PAZIENTE)
                .build();

        return utenteRepository.saveAndFlush(utente);
    }

    public void deletePazienteById(Long id) {
        getPazienteById(id);
        utenteRepository.deleteById(id);
    }

    public Utente updatePaziente(Long id, PazienteRequest newPaziente) {
        Utente oldPaziente = getPazienteById(id);

        oldPaziente.setNome(newPaziente.getNome());
        oldPaziente.setCognome(newPaziente.getCognome());
        oldPaziente.setCodiceFiscale(newPaziente.getCodiceFiscale());
        oldPaziente.setSaldo(newPaziente.getSaldo());

        return utenteRepository.saveAndFlush(oldPaziente);
    }

    public Visita prenotaVisita(Long pazienteId, PrenotaVisitaRequest prenotaVisitaRequest) {
        Utente paziente = getPazienteById(pazienteId);
        Utente medico = getMedicoById(prenotaVisitaRequest.getIdMedico());
        return visitaService.createVisita(pazienteId, prenotaVisitaRequest);
    }
}
