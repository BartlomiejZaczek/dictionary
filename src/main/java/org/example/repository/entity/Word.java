package org.example.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue
    private long id;
    private String polish;
    private String english;
}
