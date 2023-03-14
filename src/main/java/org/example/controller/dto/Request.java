package org.example.controller.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {

    @NonNull
    private String polish;
    @NonNull
    private String english;
}
