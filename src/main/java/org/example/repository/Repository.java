package org.example.repository;

import org.example.repository.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Word, Long> {

}
