package com.example.smart_hospital.repositories;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.entities.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
    List<Visita> findByPaziente(Utente paziente);
    List<Visita> findByMedico(Utente medico);
    List<Visita> findByPazienteIsNull();
}
