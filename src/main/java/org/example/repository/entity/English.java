package org.example.repository.entity;

import jakarta.persistence.*;

@Entity
public class English {
    @Id
    @GeneratedValue
    private long id;
    private String word;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "POLISH_WORD")
    private Polish polish;
}
