package org.example.service;

import org.example.DictionaryApplication;
import org.example.controller.dto.UntranslatedWordRequest;
import org.example.controller.dto.WordRequest;
import org.example.repository.Repository;
import org.example.repository.UntranslatedWordRepository;
import org.example.repository.entity.UntranslatedWord;
import org.example.repository.entity.Word;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DictionaryApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceTest {
    @Autowired
    Repository repository;
    @Autowired
    UntranslatedWordRepository untranslatedWordRepository;
    Service service;

    @BeforeEach
     public void setup(){
            MockitoAnnotations.openMocks(this);
            service = new Service(repository, untranslatedWordRepository);
    }
    @Test
    @DirtiesContext
    public void testSave() {
        service.save(new WordRequest("kot", "cat"));
        List<Word> result =  repository.findAll();
        Word expectedResult = new Word(1,"kot", "cat");
        Assertions.assertEquals(result.get(0), expectedResult);
    }
    @Test
    @DirtiesContext
    public void testSaveAddExistingThrowsException() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "cat")));
    }
    @Test
    @DirtiesContext
    public void testSaveAddExistingThrowsException2() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "kitty")));
    }
    @Test
    @DirtiesContext
    public void testSaveAddExistingThrowsException3() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kotek", "cat")));
    }
    @Test
    @DirtiesContext
    public void testSaveAddBlankThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("", "")));
    }
    @Test
    @DirtiesContext
    public void testSaveAddBlankThrowsException2() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("", "cat")));
    }
    @Test
    @DirtiesContext
    public void testSaveAddBlankThrowsException3() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "")));
    }
    @Test
    @DirtiesContext
    public void testSaveUntranslated() {
        service.saveUntranslated(new UntranslatedWordRequest("the"));
        List<UntranslatedWord> result =  untranslatedWordRepository.findAll();
        UntranslatedWord expectedResult = new UntranslatedWord(1, "the");
        Assertions.assertEquals(result.get(0), expectedResult);
    }
    @Test
    @DirtiesContext
    public void testSaveUntranslatedAddExistingThrowsException() {
        service.saveUntranslated(new UntranslatedWordRequest("the"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.saveUntranslated(new UntranslatedWordRequest("the")));
    }
    @Test
    @DirtiesContext
    public void testSaveUntranslatedAddBlankThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.saveUntranslated(new UntranslatedWordRequest("")));
    }
    @Test
    @DirtiesContext
    public void testTranslatePolishToEnglish() {
        repository.save(new Word(1, "pies", "dog"));
        String result = service.translate("pies");
        String expectedResult = "Translation: 'dog' was found in english dictionary";
        assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    public void testTranslateEnglishToPolish() {
        repository.save(new Word(1, "pies", "dog"));
        String result = service.translate("dog");
        String expectedResult = "Translation: 'pies' was found in polish dictionary";
        assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    public void testTranslateThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.translate("kot"));
    }
    @Test
    @DirtiesContext
    public void testFindAll() {
        repository.save(new Word(1, "pies", "dog"));
        List<Word> expectedResult = new ArrayList<>();
        expectedResult.add(new Word(1, "pies", "dog"));
        List<Word> result = service.findAll();
        assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    public void testFindAllUntranslated() {
        untranslatedWordRepository.save(new UntranslatedWord(1, "the"));
        List<UntranslatedWord> expectedResult = new ArrayList<>();
        expectedResult.add(new UntranslatedWord(1, "the"));
        List<UntranslatedWord> result = service.findAllUntranslated();
        assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    void testSentenceTranslate() {
        repository.save(new Word(1, "ten", "this"));
        repository.save(new Word(2, "duży", "big"));
        repository.save(new Word(3, "długi", "long"));
        repository.save(new Word(4, "ogon", "tail"));
        String sentence = "this CAT IS big i ma długi ogon";
        String expectedResult = "ten cat is duży i ma long tail";
        String result = service.sentenceTranslate(sentence);
        Assertions.assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    void testSentenceTranslateEmptySentenceThrowException() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.sentenceTranslate(""));
    }
    @Test
    @DirtiesContext
    void testSentenceTranslateSaveUntranslated() {
        String sentence = "this CAT IS big";
        repository.save(new Word(1, "ten", "this"));
        service.sentenceTranslate(sentence);
        List<UntranslatedWord> result =  untranslatedWordRepository.findAll();
        List<UntranslatedWord> expectedResult = new ArrayList<>();
        expectedResult.add(new UntranslatedWord(1,"cat"));
        expectedResult.add(new UntranslatedWord(2,"is"));
        expectedResult.add(new UntranslatedWord(3,"big"));
        Assertions.assertEquals(result, expectedResult);
    }
    @Test
    @DirtiesContext
    void testCreateReport() {
        repository.save(new Word(1, "kot", "cat"));
        repository.save(new Word(2, "pies", "dog"));
        repository.save(new Word(3, "słoń", "elephant"));
        untranslatedWordRepository.save(new UntranslatedWord(1, "kura"));
        untranslatedWordRepository.save(new UntranslatedWord(2, "krowa"));
        String result = service.createReport();
        String expectedResult = "All words count: 6\n" +
                "Polish words count: 3\n" +
                "English words count: 3\n" +
                "Average polish word length: 3\n" +
                "Average english word length: 4\n" +
                "Untranslated words count: 2\n" +
                "Polish words all lengths count:\n" +
                " [1 words with length: 3]\n" +
                " [2 words with length: 4]\n" +
                "\n" +
                "English words all lengths count:\n" +
                " [2 words with length: 3]\n" +
                " [1 words with length: 8]\n";
        Assertions.assertEquals(result, expectedResult);
    }
    @Test
    void testCreateReportEmpty() {
        String result = service.createReport();
        String expectedResult = "All words count: 0\n" +
                "Polish words count: 0\n" +
                "English words count: 0\n" +
                "Average polish word length: 0\n" +
                "Average english word length: 0\n" +
                "Untranslated words count: 0\n" +
                "Polish words all lengths count:\n" +
                "\n" +
                "English words all lengths count:\n";
        Assertions.assertEquals(result, expectedResult);
    }
}

