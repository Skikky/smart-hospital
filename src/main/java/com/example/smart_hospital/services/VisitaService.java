package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.repositories.VisitaRepository;
import com.example.smart_hospital.requests.PrenotaVisitaRequest;
import com.example.smart_hospital.requests.VisitaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private PazienteService pazienteService;

    @Autowired
    private MedicoService medicoService;

    public List<Visita> getAllVisite() {
        return visitaRepository.findAll();
    }

    public Visita getVisitaById(Long id) {
        return visitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita con id " + id + " non trovata"));
    }

    public Visita createVisita(Long pazienteId, PrenotaVisitaRequest visitaRequest) {
        Utente medico = medicoService.getMedicoById(visitaRequest.getIdMedico());

        Utente paziente = pazienteService.getPazienteById(pazienteId);
        Visita visita = Visita.builder()
                .dataOra(visitaRequest.getDataOra())
                .prezzo(0)
                .paziente(paziente)
                .medico(medico)
                .referto(null)
                .build();

        return visitaRepository.saveAndFlush(visita);
    }

    public Visita updateVisita(Long id, VisitaRequest newVisita) {
        Visita oldVisita = getVisitaById(id);

        Utente paziente = pazienteService.getPazienteById(newVisita.getIdPaziente());

        Utente medico = medicoService.getMedicoById(newVisita.getIdMedico());

        oldVisita.setDataOra(newVisita.getDataOra());
        oldVisita.setPrezzo(newVisita.getPrezzo());
        oldVisita.setPaziente(paziente);
        oldVisita.setMedico(medico);

        return visitaRepository.saveAndFlush(oldVisita);
    }

    public void deleteVisitaById(Long id) {
        getVisitaById(id);
        visitaRepository.deleteById(id);
    }
}
