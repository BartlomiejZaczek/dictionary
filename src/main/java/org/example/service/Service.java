package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.UntranslatedWordRequest;
import org.example.controller.dto.WordRequest;
import org.example.repository.Repository;
import org.example.repository.UntranslatedWordRepository;
import org.example.repository.entity.UntranslatedWord;
import org.example.repository.entity.Word;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final Repository repository;
    private final UntranslatedWordRepository untranslatedWordRepository;

    public void save(WordRequest wordRequest) {
        if (wordRequest.getEnglish().isBlank()|| wordRequest.getPolish().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fields must not be empty");
        }
        if (repository.findAny(wordRequest.getEnglish()) != null ||
            repository.findAny(wordRequest.getPolish()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Word already exists in dictionary");
        }
        repository
                .save(Word
                        .builder()
                        .polish(wordRequest.getPolish().toLowerCase())
                        .english(wordRequest.getEnglish().toLowerCase())
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

    public String sentenceTranslate(String sentence) {
        String[] sentenceArray = sentence.split(" ");
        String[] newSentence = new String[sentenceArray.length];
        int i = 0;
        for ( String word : sentenceArray) {
            Word polish = repository.findByPolish(word.toLowerCase());
            Word english = repository.findByEnglish(word.toLowerCase());
            word.toLowerCase();
            if (polish != null) {
                 newSentence[i] = polish.getEnglish();
            } else if (english != null) {
                 newSentence[i] = english.getPolish();
            } else {
                newSentence[i] = sentenceArray[i];
                saveUntranslated(new UntranslatedWordRequest(sentenceArray[i]));
            }
            i++;
        }
        return String.join(" ", newSentence);
    }
    public void saveUntranslated(UntranslatedWordRequest untranslatedWordRequest) {
        untranslatedWordRepository
                .save(UntranslatedWord
                        .builder()
                        .word(untranslatedWordRequest.getWord().toLowerCase())
                        .build());
    }
    public List<UntranslatedWord> findAllUntranslated() {
        return untranslatedWordRepository.findAll();
    }

}
