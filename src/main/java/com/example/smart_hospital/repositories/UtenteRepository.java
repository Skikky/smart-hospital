package com.example.smart_hospital.repositories;

import com.example.smart_hospital.entities.Utente;
import com.example.smart_hospital.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    List<Utente> findByRole(Role role);
    List<Utente> findBySpecializzazione(String specializzazione);
    Utente findByEmail(String email);
}
