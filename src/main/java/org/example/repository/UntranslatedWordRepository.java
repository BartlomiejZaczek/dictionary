package org.example.repository;

import org.example.repository.entity.UntranslatedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UntranslatedWordRepository extends JpaRepository<UntranslatedWord, Long> {
    List<UntranslatedWord> findAll();
}
