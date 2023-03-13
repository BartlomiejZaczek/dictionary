package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.EnglishRequest;
import org.example.controller.dto.EnglishResponse;
import org.example.repository.EnglishRepository;
import org.example.repository.entity.English;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnglishService {
    private final EnglishRepository repository;

    public List<English> findAll() {
        return repository.findAll();
    }
    public void save(EnglishRequest englishRequest) {
        repository
                .save(English
                        .builder()
                        .word(englishRequest.getWord())
                        .build());
    }
}

