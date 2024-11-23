package com.example.deple.service;

import com.example.deple.converter.ChallengeConverter;
import com.example.deple.dto.challenge.ChallengeRequestDTO;
import com.example.deple.dto.challenge.ChallengeResponseDTO;
import com.example.deple.entity.Challenge;
import com.example.deple.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeConverter challengeConverter;

    @Transactional
    public ChallengeResponseDTO createChallenge(Integer challengeDay, ChallengeRequestDTO.ChallengeCreateDto challengeRequestDTO) {
        Challenge challenge = challengeConverter.toChallengeEntity(challengeDay,challengeRequestDTO);
        Challenge savedChallenge = challengeRepository.save(challenge);
        return challengeConverter.toChallengeResponseDTO(savedChallenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Integer challengeDay) {
        Challenge challenge = challengeRepository.findByNumber(challengeDay)
                .orElseThrow(() -> new IllegalArgumentException("해당 Day에 해당하는 챌린지를 찾을 수 없습니다."));

        return challengeConverter.toChallengeResponseDTO(challenge);
    }
}

