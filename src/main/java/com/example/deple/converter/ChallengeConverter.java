package com.example.deple.converter;

import com.example.deple.dto.challenge.ChallengeRequestDTO;
import com.example.deple.dto.challenge.ChallengeResponseDTO;
import com.example.deple.entity.Challenge;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ChallengeConverter {
    public Challenge toChallengeEntity(int challengeDay, ChallengeRequestDTO.ChallengeCreateDto dto) {
        return Challenge.builder()
                .number(challengeDay)
                .title(dto.getTitle())
                .details(dto.getDescription())
                .build();
    }

    public ChallengeResponseDTO toChallengeResponseDTO(Challenge challengeEntity) {
        return ChallengeResponseDTO.builder().entity(challengeEntity).build();
    }
}
