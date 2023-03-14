package org.example.service;

import org.example.DictionaryApplication;
import org.example.controller.dto.UntranslatedWordRequest;
import org.example.controller.dto.WordRequest;
import org.example.repository.Repository;
import org.example.repository.UntranslatedWordRepository;
import org.example.repository.entity.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DictionaryApplication.class)
public class ServiceTest {
    @Autowired
    Repository repository;
    @Autowired
    UntranslatedWordRepository untranslatedWordRepository;

    Service service;




    @BeforeEach
    public void setup(){
            MockitoAnnotations.initMocks(this);
            service = new Service(repository);
        }


//        try {
//            this.repository.save(Word
//                    .builder()
//                    .polish("pies")
//                    .english("dog")
////                    .english(wordRequest.getEnglish().toLowerCase())
//                    .build());
//        } catch (NullPointerException e) {
//
//        }
//@Test
//void testSaveUntranslated() {
//    service.saveUntranslated(new UntranslatedWordRequest("word"));
//}
    @Test
    void testSave() {
        service.save(new WordRequest("word", "word"));
        System.out.println(service.translate("word"));

    }

    @Test
    public void test() {
//        Repository repository1 = mock(Repository.class);
//        Service service1 = new Service(new R)
//        service1(repository1);
        repository.save(Word
                .builder()
                .polish("pies")
                .english("dog")
//                    .english(wordRequest.getEnglish().toLowerCase())
                .build());

        System.out.println(repository);
        String test = "word";
        String test2 = "word";
        String expectedResult = "dog";
//        String result = this.service.translate("pies");
        Assertions.assertEquals(test, test2);
    }
}
