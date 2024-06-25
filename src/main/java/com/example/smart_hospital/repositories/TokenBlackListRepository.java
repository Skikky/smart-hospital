package com.example.smart_hospital.repositories;

import com.example.smart_hospital.entities.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    @Query(value = "SELECT * FROM token_black_list WHERE utente_id = :utente_id", nativeQuery = true)
    List<TokenBlackList> getTokenBlackListFromUtenteId(@Param("utente_id") Long utente_id);
}