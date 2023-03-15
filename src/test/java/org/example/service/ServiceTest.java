package org.example.service;

import org.example.DictionaryApplication;
import org.example.controller.dto.UntranslatedWordRequest;
import org.example.controller.dto.WordRequest;
import org.example.repository.Repository;
import org.example.repository.UntranslatedWordRepository;
import org.example.repository.entity.UntranslatedWord;
import org.example.repository.entity.Word;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

//@RunWith(SpringRunner.class)
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
//            repository = mock(Repository.class);
            MockitoAnnotations.openMocks(this);
            service = new Service(repository, untranslatedWordRepository);
//            service = mock(Service.class);
    }
    @Test
    public void testSave() {
        service.save(new WordRequest("kot", "cat"));
        List<Word> result =  repository.findAll();
        Word expectedResult = new Word(1,"kot", "cat");
        Assertions.assertEquals(result.get(0), expectedResult);
    }
    @Test
    public void testSaveAddExistingThrowsException() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "cat")));
    }
    @Test
    public void testSaveAddExistingThrowsException2() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "kitty")));
    }
    @Test
    public void testSaveAddExistingThrowsException3() {
        service.save(new WordRequest("kot", "cat"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kotek", "cat")));
    }
    @Test
    public void testSaveAddBlankThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("", "")));
    }
    @Test
    public void testSaveAddBlankThrowsException2() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("", "cat")));
    }
    @Test
    public void testSaveAddBlankThrowsException3() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.save(new WordRequest("kot", "")));
    }
    @Test
    public void testSaveUntranslated() {
        service.saveUntranslated(new UntranslatedWordRequest("the"));
        List<UntranslatedWord> result =  untranslatedWordRepository.findAll();
        UntranslatedWord expectedResult = new UntranslatedWord(1, "the");
        Assertions.assertEquals(result.get(0), expectedResult);
    }
    @Test
    public void testSaveUntranslatedAddExistingThrowsException() {
        service.saveUntranslated(new UntranslatedWordRequest("the"));
        Assertions.assertThrows(ResponseStatusException.class, () -> service.saveUntranslated(new UntranslatedWordRequest("the")));
    }
    @Test
    public void testSaveUntranslatedAddBlankThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class, () -> service.saveUntranslated(new UntranslatedWordRequest("")));
    }
    @Test
    public void testTranslatePolishToEnglish() {
        repository.save(new Word(1, "pies", "dog"));
        String result = service.translate("pies");
        String expectedResult = "Translation: 'dog' was found in english dictionary";
        assertEquals(result, expectedResult);
    }
    @Test
    public void testTranslateEnglishToPolish() {
        repository.save(new Word(1, "pies", "dog"));
        String result = service.translate("dog");
        String expectedResult = "Translation: 'pies' was found in polish dictionary";
        assertEquals(result, expectedResult);
    }
    @Test
    public void testTranslateThrowsException() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.translate("kot"));
    }
    @Test
    public void testFindAll() {
        repository.save(new Word(1, "pies", "dog"));
        List<Word> expectedResult = new ArrayList<>();
        expectedResult.add(new Word(1, "pies", "dog"));
        List<Word> result = service.findAll();
        assertEquals(result, expectedResult);
    }
    @Test
    public void testFindAllUntranslated() {
        untranslatedWordRepository.save(new UntranslatedWord(1, "the"));
        List<UntranslatedWord> expectedResult = new ArrayList<>();
        expectedResult.add(new UntranslatedWord(1, "the"));
        List<UntranslatedWord> result = service.findAllUntranslated();
        assertEquals(result, expectedResult);
    }
    @Test
    public void test() {

    }
}

