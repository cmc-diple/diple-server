package com.example.deple.dto.challenge;

import com.example.deple.entity.Challenge;
import com.example.deple.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeResponseDTO {
    private int Number;
    private String Title;
    private String Description;
    private Status Status;

    @Builder
    public ChallengeResponseDTO(Challenge entity) {
        this.Number = entity.getNumber();
        this.Title = entity.getTitle();
        this.Description =entity.getDetails();
        this.Status = entity.getStatus();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllChallengeListDto {
        List<Challenge> challengeList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChallengeSummaryDTO {
        private int totalCount; // 전체 갯수
        private List<ChallengeResponseDTO> challenges; // 여러 Challenge DTO
    }

}

