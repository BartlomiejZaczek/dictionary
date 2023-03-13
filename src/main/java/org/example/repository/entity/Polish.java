package org.example.repository.entity;

import jakarta.persistence.*;

@Entity
public class Polish {
    @Id
    @GeneratedValue
    private long id;
    private String word;
    @OneToOne
    @JoinColumn(name = "ENGLISH_WORD")
    private English english;
}