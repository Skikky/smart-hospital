package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.requests.PazienteRequest;
import com.example.smart_hospital.responses.MedicoResponse;
import com.example.smart_hospital.responses.PazienteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PazienteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private VisitaService visitaService;
    @Autowired
    private MedicoService medicoService;

    private PazienteResponse convertToPazienteResponse(Utente utente) {
        if (utente.getRole() != Role.PAZIENTE) {
            throw new IllegalArgumentException("L'utente non è un paziente");
        }
        return PazienteResponse.builder()
                .id(utente.getId())
                .nome(utente.getNome())
                .cognome(utente.getCognome())
                .codiceFiscale(utente.getCodiceFiscale())
                .role(utente.getRole())
                .saldo(utente.getSaldo())
                .build();
    }

    public List<PazienteResponse> getAllPazienti() {
        List<Utente> pazienti = utenteRepository.findByRole(Role.PAZIENTE);
        return pazienti.stream().map(this::convertToPazienteResponse).collect(Collectors.toList());
    }

    public Utente getPazienteById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRole() != Role.PAZIENTE) {
            throw new IllegalStateException("L'utente con id " + id + " non è un paziente");
        }

        return utente;
    }

    public PazienteResponse getPazienteResponseById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRole() != Role.PAZIENTE) {
            throw new IllegalStateException("L'utente con id " + id + " non è un paziente");
        }

        return convertToPazienteResponse(utente);
    }

    public PazienteResponse createPaziente(PazienteRequest pazienteRequest) {
        Utente utente = Utente.builder()
                .nome(pazienteRequest.getNome())
                .cognome(pazienteRequest.getCognome())
                .codiceFiscale(pazienteRequest.getCodiceFiscale())
                .saldo(pazienteRequest.getSaldo())
                .role(Role.PAZIENTE)
                .build();

        Utente savedUtente = utenteRepository.saveAndFlush(utente);
        return convertToPazienteResponse(savedUtente);
    }

    public void deletePazienteById(Long id) {
        getPazienteById(id);
        utenteRepository.deleteById(id);
    }

    public PazienteResponse updatePaziente(Long id, PazienteRequest newPaziente) {
        Utente oldPaziente = getPazienteById(id);

        oldPaziente.setNome(newPaziente.getNome());
        oldPaziente.setCognome(newPaziente.getCognome());
        oldPaziente.setCodiceFiscale(newPaziente.getCodiceFiscale());
        oldPaziente.setSaldo(newPaziente.getSaldo());

        Utente updatedUtente = utenteRepository.saveAndFlush(oldPaziente);
        return convertToPazienteResponse(updatedUtente);
    }

    public List<MedicoResponse> findSpecialisti(String specializzazione) {
        return medicoService.findSpecialisti(specializzazione);
    }
}
