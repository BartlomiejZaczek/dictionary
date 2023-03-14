package org.example.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.example.controller.dto.UntranslatedWordRequest;
import org.example.controller.dto.WordRequest;
import org.example.repository.Repository;
import org.example.repository.UntranslatedWordRepository;
import org.example.repository.entity.UntranslatedWord;
import org.example.repository.entity.Word;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final Repository repository;
    private final UntranslatedWordRepository untranslatedWordRepository;

    public void save(WordRequest wordRequest) {
        if (wordRequest.getEnglish().isBlank() || wordRequest.getPolish().isBlank()) {
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
        for (String word : sentenceArray) {
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

    public String createReport() {
        List<String> polish = repository.findAllPolishWords();
        List<String> english = repository.findAllEnglishWords();
        int averagePolishWordSize = 0;
        int averageEnglishWordSize = 0;

        int polishLetterCount = 0;
        if (polish.size() > 0) {
            for (String word : polish) {
                polishLetterCount += word.length();
                averagePolishWordSize = polishLetterCount / polish.size();
            }
        }
        int englishLetterCount = 0;
        if (english.size() > 0) {
            for (String word : english) {
                englishLetterCount += word.length();
            }
            averageEnglishWordSize = englishLetterCount / english.size();
        }
        Map<Integer, Integer> polishWordsLengthCount = new HashMap<>();
        for (String word :
                polish) {
            int length = word.length();
            if (polishWordsLengthCount.containsKey(length)) {
                polishWordsLengthCount.put(length, polishWordsLengthCount.get(length) + 1);
            } else {
                polishWordsLengthCount.put(length, 1);
            }
        }
        Map<Integer, Integer> englishWordLengthCount = new HashMap<>();
        for (String word :
                polish) {
            int length = word.length();
            if (englishWordLengthCount.containsKey(length)) {
                englishWordLengthCount.put(length, englishWordLengthCount.get(length) + 1);
            } else {
                englishWordLengthCount.put(length, 1);
            }
        }
        StringBuffer polishWordsLengths = new StringBuffer();
        for (Map.Entry<Integer, Integer> entry :
                polishWordsLengthCount.entrySet()) {
            polishWordsLengths.append(" [" + entry.getValue() + " words with length: " + entry.getKey() + "]\n");
        }

        StringBuffer englishWordsLengths = new StringBuffer();
        for (Map.Entry<Integer, Integer> entry :
                englishWordLengthCount.entrySet()) {
            englishWordsLengths.append(" [" + entry.getValue() + " words with length: " + entry.getKey() + "]\n");
        }


        return "All words count: " + (polish.size() + english.size()) +
                "\nPolish words count: " + polish.size() +
                "\nEnglish words count: " + english.size() +
                "\nAverage polish word length: " + averagePolishWordSize +
                "\nAverage english word length: " + averageEnglishWordSize +
                "\nUntranslated words count: " + findAllUntranslated().size() +
                "\nPolish words all lengths count:\n" + polishWordsLengths +
                "\nEnglish words all lengths count:\n" + englishWordsLengths;
    }

    public File createPdf() {
        try {
            String report = createReport();
            Document document = new Document();
            OutputStream outputStream =
                    new FileOutputStream(new File("Report.pdf"));
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph(report));
            document.close();
            outputStream.close();
            return new File("Report.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
