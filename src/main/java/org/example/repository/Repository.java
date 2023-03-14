package org.example.repository;

import org.example.repository.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Repository extends JpaRepository<Word, Long> {
    List<Word> findAll();
    @Query("SELECT w FROM Word w WHERE w.polish= :polish")
    Word findByPolish(@Param("polish") String polish);
    @Query("SELECT w FROM Word w WHERE w.english= :english")
    Word findByEnglish(@Param("english") String english);

    @Query("SELECT w FROM Word w WHERE w.polish= :word OR w.english= :word")
    Word findAny(@Param("word") String word);
    @Query(value = "SELECT Polish FROM Words", nativeQuery = true)
    List<String> findAllPolishWords();
    @Query(value = "SELECT English FROM Words", nativeQuery = true)
    List<String> findAllEnglishWords();
}
