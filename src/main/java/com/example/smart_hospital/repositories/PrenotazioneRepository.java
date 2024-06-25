package com.example.smart_hospital.repositories;

import com.example.smart_hospital.entities.Prenotazione;
import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByPaziente(Utente paziente);
    List<Prenotazione> findByVisita(Visita visita);
}
