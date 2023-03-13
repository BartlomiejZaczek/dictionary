package org.example.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.repository.entity.Polish;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnglishRequest {
//    private long id;
    private String word;
//    private Polish polish;
}
