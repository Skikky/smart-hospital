package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Prenotazione;
import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import com.example.smart_hospital.repositories.PrenotazioneRepository;
import com.example.smart_hospital.repositories.VisitaRepository;
import com.example.smart_hospital.requests.PazienteRequest;
import com.example.smart_hospital.requests.PrenotazioneRequest;
import com.example.smart_hospital.responses.PrenotazioneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private PazienteService pazienteService;
    @Autowired
    private VisitaRepository visitaRepository;

    private PrenotazioneResponse convertToPrenotazioneResponse(Prenotazione prenotazione) {
        return PrenotazioneResponse.builder()
                .id(prenotazione.getId())
                .idPaziente(prenotazione.getPaziente().getId())
                .idVisita(prenotazione.getVisita().getId())
                .dataPrenotazione(prenotazione.getDataPrenotazione())
                .build();
    }

    @Transactional
    public PrenotazioneResponse prenotaVisita(PrenotazioneRequest prenotazioneRequest) {
        Utente paziente = pazienteService.getPazienteById(prenotazioneRequest.getIdPaziente());
        Visita visita = visitaRepository.getReferenceById(prenotazioneRequest.getIdVisita());

        LocalDateTime dataPrenotazione = prenotazioneRequest.getDataPrenotazione();
        LocalDateTime finePrenotazione = dataPrenotazione.plusMinutes(visita.getDurata());

        if (dataPrenotazione.isBefore(visita.getInizioDisponibilita()) || finePrenotazione.isAfter(visita.getFineDisponibilita())) {
            throw new IllegalArgumentException("La data di prenotazione deve essere tra l'inizio e la fine della disponibilità.");
        }

        if (paziente.getSaldo() < visita.getPrezzo()) {
            throw new IllegalArgumentException("Il saldo del paziente è insufficiente per prenotare questa visita.");
        }

        // Verifica se c'è già una prenotazione che si sovrappone a quella richiesta
        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository.findByVisita(visita);
        for (Prenotazione prenotazione : prenotazioniEsistenti) {
            LocalDateTime inizioPrenotazioneEsistente = prenotazione.getDataPrenotazione();
            LocalDateTime finePrenotazioneEsistente = inizioPrenotazioneEsistente.plusHours(visita.getDurata());

            if ((dataPrenotazione.isBefore(finePrenotazioneEsistente) && finePrenotazione.isAfter(inizioPrenotazioneEsistente))) {
                throw new IllegalArgumentException("Esiste già una prenotazione per questa visita che si sovrappone alla data specificata.");
            }
        }

        paziente.setSaldo(paziente.getSaldo() - visita.getPrezzo());

        PazienteRequest pazienteRequest = PazienteRequest.builder()
                .nome(paziente.getNome())
                .cognome(paziente.getCognome())
                .codiceFiscale(paziente.getCodiceFiscale())
                .saldo(paziente.getSaldo())
                .build();

        pazienteService.updatePaziente(paziente.getId(), pazienteRequest);

        Prenotazione prenotazione = Prenotazione.builder()
                .paziente(paziente)
                .visita(visita)
                .dataPrenotazione(dataPrenotazione)
                .build();

        visita.setPaziente(paziente);
        visitaRepository.saveAndFlush(visita);

        Prenotazione savedPrenotazione = prenotazioneRepository.saveAndFlush(prenotazione);
        return convertToPrenotazioneResponse(savedPrenotazione);
    }

    public Prenotazione getPrenotazioneById(Long id) {
        return prenotazioneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione con id " + id + " non trovata"));
    }

    public List<PrenotazioneResponse> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findAll();
        return prenotazioni.stream().map(this::convertToPrenotazioneResponse).collect(Collectors.toList());
    }

    public PrenotazioneResponse getPrenotazioneResponseById(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione con id " + id + " non trovata"));
        return convertToPrenotazioneResponse(prenotazione);
    }

    public List<PrenotazioneResponse> getPrenotazioniByPaziente(Long idPaziente) {
        Utente paziente = pazienteService.getPazienteById(idPaziente);
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByPaziente(paziente);
        return prenotazioni.stream().map(this::convertToPrenotazioneResponse).collect(Collectors.toList());
    }

    public void deletePrenotazione(Long id) {
        getPrenotazioneById(id);
        prenotazioneRepository.deleteById(id);
    }
}
