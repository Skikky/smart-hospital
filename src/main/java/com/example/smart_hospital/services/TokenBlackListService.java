package com.example.smart_hospital.services;

import com.example.smart_hospital.entities.TokenBlackList;
import com.example.smart_hospital.repositories.TokenBlackListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TokenBlackListService {
    @Autowired
    private TokenBlackListRepository tokenBlackListRepository;
    private static final Object lock = new Object();

    public List<String> tokenNotValidFromUtenteById(Long id_utente) {
        return tokenBlackListRepository.getTokenBlackListFromUtenteId(id_utente)
                .stream()
                .map(TokenBlackList::getToken)
                .toList();
    }

    public void createTokenBlackList(TokenBlackList tokenBlackList) {
        tokenBlackList.setInsertTime(LocalDateTime.now());
        tokenBlackListRepository.saveAndFlush(tokenBlackList);
    }

    public Boolean isTokenPresent(String token) {
        return tokenBlackListRepository.findAll().stream().map(TokenBlackList::getToken).toList().contains(token);
    }

    public List<TokenBlackList> getAll() {
        return tokenBlackListRepository.findAll();
    }

    public void executeScheduledTask() {
        synchronized (lock) {
            if (tokenBlackListRepository == null) return;
            if (!getAll().isEmpty()) {
                deleteTokens();
            }
        }
    }

    public void deleteTokens() {
        List<TokenBlackList> tokens = tokenBlackListRepository.findAll();
        if (tokens.isEmpty()) throw new NullPointerException();

        List<TokenBlackList> deleteTokens = tokens.stream()
                .filter(t -> Objects.nonNull(t.getInsertTime()) &&
                        Duration.between(t.getInsertTime(), LocalDateTime.now()).getSeconds() >= 20)
                .toList();

        tokenBlackListRepository.deleteAllInBatch(deleteTokens);
    }
}
