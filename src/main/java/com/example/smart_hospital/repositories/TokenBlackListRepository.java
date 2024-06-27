package com.example.smart_hospital.repositories;

import com.example.smart_hospital.entities.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    @Query("SELECT t FROM TokenBlackList t WHERE t.utente.id = :utenteId")
    List<TokenBlackList> getTokenBlackListFromUtenteId(@Param("utenteId") Long utenteId);
}