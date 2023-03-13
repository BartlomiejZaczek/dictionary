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
public class English {
    @Id
    @GeneratedValue
    private long id;
    private String word;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "POLISH_WORD")
    private Polish polish;
}
