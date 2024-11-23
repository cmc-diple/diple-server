package com.example.deple.dto.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeRequestDTO {
    @Getter
    public static class ChallengeCreateDto {
        private String title;
        private String description;
    }
}