package org.example.repository;

import org.example.repository.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Repository extends JpaRepository<Word, Long> {
    List<Word> findAll();

}
