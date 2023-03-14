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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fields must not be empty");
        }
        if (repository.findAny(request.getEnglish()) != null ||
            repository.findAny(request.getPolish()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Word already exists in dictionary");
        }
        repository
                .save(Word
                        .builder()
                        .polish(request.getPolish().toLowerCase())
                        .english(request.getEnglish().toLowerCase())
                        .build());
    }

    public List<Word> findAll() {
        return repository.findAll();
    }

    public String translate(String word) {
        Word polish = repository.findByPolish(word.toLowerCase());
        Word english = repository.findByEnglish(word.toLowerCase());
        if (polish == null && english == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Word not recognized");
        }
        if (polish != null) {
            return "Translation: '" + polish.getEnglish() + "' was found in english dictionary";

        } else if (english != null) {
            return "Translation: '" + english.getPolish() + "' was found in polish dictionary";
        }
        return null;
    }
}

