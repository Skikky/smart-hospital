package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.repositories.VisitaRepository;
import com.example.smart_hospital.requests.VisitaRequest;
import com.example.smart_hospital.responses.VisitaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private MedicoService medicoService;

    private VisitaResponse convertToVisitaResponse(Visita visita) {
        return VisitaResponse.builder()
                .id(visita.getId())
                .inizioDisponibilita(visita.getInizioDisponibilita())
                .fineDisponibilita(visita.getFineDisponibilita())
                .prezzo(visita.getPrezzo())
                .idPaziente(visita.getPaziente() != null ? visita.getPaziente().getId() : null)
                .idMedico(visita.getMedico().getId())
                .build();
    }

    public List<VisitaResponse> getAllVisite() {
        List<Visita> visite = visitaRepository.findAll();
        return visite.stream().map(this::convertToVisitaResponse).collect(Collectors.toList());
    }

    public Visita getVisitaById(Long id) {
        return visitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita con id " + id + " non trovata"));
    }

    public VisitaResponse getVisitaResponseById(Long id) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita con id " + id + " non trovata"));
        return convertToVisitaResponse(visita);
    }

    public VisitaResponse createVisita(VisitaRequest visitaRequest) {
        Utente medico = medicoService.getMedicoById(visitaRequest.getIdMedico());

        Visita visita = Visita.builder()
                .inizioDisponibilita(visitaRequest.getInizioDisponibilita())
                .fineDisponibilita(visitaRequest.getFineDisponibilita())
                .prezzo(visitaRequest.getPrezzo())
                .medico(medico)
                .paziente(null)
                .build();

        Visita savedVisita = visitaRepository.saveAndFlush(visita);
        return convertToVisitaResponse(savedVisita);
    }

    public VisitaResponse updateVisita(Long id, VisitaRequest newVisita) {
        Visita oldVisita = getVisitaById(id);

        Utente medico = medicoService.getMedicoById(newVisita.getIdMedico());

        oldVisita.setInizioDisponibilita(newVisita.getInizioDisponibilita());
        oldVisita.setFineDisponibilita(newVisita.getFineDisponibilita());
        oldVisita.setPrezzo(newVisita.getPrezzo());
        oldVisita.setMedico(medico);

        Visita updatedVisita = visitaRepository.saveAndFlush(oldVisita);
        return convertToVisitaResponse(updatedVisita);
    }

    public void deleteVisitaById(Long id) {
        getVisitaById(id);
        visitaRepository.deleteById(id);
    }

    public List<VisitaResponse> trovaVisitePerMedico(Utente medico) {
        List<Visita> visite = visitaRepository.findByMedico(medico);
        return visite.stream().map(this::convertToVisitaResponse).collect(Collectors.toList());
    }

    public List<VisitaResponse> trovaVisiteDisponibili() {
        List<Visita> visite = visitaRepository.findByPazienteIsNull();
        return visite.stream().map(this::convertToVisitaResponse).collect(Collectors.toList());
    }

    public List<VisitaResponse> trovaVisitePerPaziente(Utente paziente) {
        List<Visita> visite = visitaRepository.findByPaziente(paziente);
        return visite.stream().map(this::convertToVisitaResponse).collect(Collectors.toList());
    }
}
