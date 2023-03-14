package org.example.controller.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UntranslatedWordRequest {
    private String word;
}
