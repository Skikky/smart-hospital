package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.requests.MedicoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Utente> getAllMedici() {
        return utenteRepository.findByRuolo(Role.MEDICO);
    }

    public Utente getMedicoById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRuolo() != Role.MEDICO) {
            throw new IllegalStateException("L'utente con id " + id + " non è un medico");
        }

        return utente;
    }

    public Utente createMedico(MedicoRequest medicoRequest) {
        Utente utente = Utente.builder()
                .nome(medicoRequest.getNome())
                .cognome(medicoRequest.getCognome())
                .codiceFiscale(medicoRequest.getCodiceFiscale())
                .specializzazione(medicoRequest.getSpecializzazione())
                .ruolo(Role.MEDICO)
                .build();

        return utenteRepository.saveAndFlush(utente);
    }

    public Utente updateMedico(Long id, MedicoRequest newMedico) {
        Utente oldMedico = getMedicoById(id);

        oldMedico.setNome(newMedico.getNome());
        oldMedico.setCognome(newMedico.getCognome());
        oldMedico.setCodiceFiscale(newMedico.getCodiceFiscale());
        oldMedico.setSpecializzazione(newMedico.getSpecializzazione());

        return utenteRepository.saveAndFlush(oldMedico);
    }

    public void deleteMedicoById(Long id) {
        getMedicoById(id);
        utenteRepository.deleteById(id);
    }

}

