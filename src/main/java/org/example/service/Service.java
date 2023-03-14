package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.Request;
import org.example.repository.Repository;
import org.example.repository.entity.Word;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final Repository repository;

    public void save(Request request) {
        if (request.getEnglish().isBlank()|| request.getPolish().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        repository
                .save(Word
                        .builder()
                        .polish(request.getPolish())
                        .english(request.getEnglish())
                        .build());
    }

    public List<Word> findAll() {
        return repository.findAll();
    }
}

