package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.enums.Role;
import com.example.smart_hospital.repositories.UtenteRepository;
import com.example.smart_hospital.requests.MedicoRequest;
import com.example.smart_hospital.responses.MedicoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    @Autowired
    private UtenteRepository utenteRepository;

    private MedicoResponse convertToMedicoResponse(Utente utente) {
        if (utente.getRole() != Role.MEDICO) {
            throw new IllegalArgumentException("L'utente non è un medico");
        }
        return MedicoResponse.builder()
                .id(utente.getId())
                .nome(utente.getNome())
                .cognome(utente.getCognome())
                .codiceFiscale(utente.getCodiceFiscale())
                .role(utente.getRole())
                .email(utente.getEmail())
                .password(utente.getPassword())
                .specializzazione(utente.getSpecializzazione())
                .build();
    }

    public List<MedicoResponse> getAllMedici() {
        List<Utente> medici = utenteRepository.findByRole(Role.MEDICO);
        return medici.stream().map(this::convertToMedicoResponse).collect(Collectors.toList());
    }

    public Utente getMedicoById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRole() != Role.MEDICO) {
            throw new IllegalStateException("L'utente con id " + id + " non è un medico");
        }

        return utente;
    }

    public MedicoResponse getMedicoResponseById(Long id) {
        Utente utente = utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con id " + id + " non trovato"));

        if (utente.getRole() != Role.MEDICO) {
            throw new IllegalStateException("L'utente con id " + id + " non è un medico");
        }

        return convertToMedicoResponse(utente);
    }

    public MedicoResponse updateMedico(Long id, MedicoRequest newMedico) {
        Utente oldMedico = getMedicoById(id);

        oldMedico.setNome(newMedico.getNome());
        oldMedico.setCognome(newMedico.getCognome());
        oldMedico.setCodiceFiscale(newMedico.getCodiceFiscale());
        oldMedico.setSpecializzazione(newMedico.getSpecializzazione());

        Utente updatedMedico = utenteRepository.saveAndFlush(oldMedico);
        return convertToMedicoResponse(updatedMedico);
    }

    public void deleteMedicoById(Long id) {
        getMedicoById(id);
        utenteRepository.deleteById(id);
    }
}

