package com.example.deple.dto.challenge;

import com.example.deple.entity.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeResponseDTO {
    private int Number;
    private String Title;
    private String Description;

    @Builder
    public ChallengeResponseDTO(Challenge entity) {
        this.Number = entity.getNumber();
        this.Title = entity.getTitle();
        this.Description =entity.getDetails();
    }
}

