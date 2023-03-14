package org.example.controller.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordRequest {

    @NonNull
    private String polish;
    @NonNull
    private String english;
}
