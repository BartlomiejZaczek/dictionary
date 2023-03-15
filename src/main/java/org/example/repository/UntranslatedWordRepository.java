package org.example.repository;

import org.example.repository.entity.UntranslatedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UntranslatedWordRepository extends JpaRepository<UntranslatedWord, Long> {
    List<UntranslatedWord> findAll();
    @Query("SELECT w FROM UntranslatedWord w WHERE w.word= :word")
    UntranslatedWord findAny(@Param("word") String word);
}
