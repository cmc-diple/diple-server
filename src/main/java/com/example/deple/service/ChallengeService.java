package com.example.deple.service;

import com.example.deple.converter.ChallengeConverter;
import com.example.deple.dto.challenge.ChallengeRequestDTO;
import com.example.deple.dto.challenge.ChallengeResponseDTO;
import com.example.deple.entity.Challenge;
import com.example.deple.entity.enums.Status;
import com.example.deple.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        boolean challengeExists = challengeRepository.existsByNumber(challengeDay);
        if (challengeExists) {
            throw new IllegalArgumentException("해당 날짜에 이미 챌린지가 존재합니다.");
        }

        if (challengeRequestDTO.getTitle() == null || challengeRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("챌린지 제목은 반드시 입력해야 합니다.");
        }

        if (challengeRequestDTO.getTitle().length() > 20) {
            throw new IllegalArgumentException("제목은 20자 이내여야 합니다.");
        }

        if (challengeRequestDTO.getDescription().length() > 300) {
            throw new IllegalArgumentException("내용은 300자 이내여야 합니다.");
        }

        Challenge challenge = challengeConverter.toChallengeEntity(challengeDay, challengeRequestDTO);
        Challenge savedChallenge = challengeRepository.save(challenge);
        return challengeConverter.toChallengeResponseDTO(savedChallenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO getChallenge(Integer challengeDay) {
        Challenge challenge = challengeRepository.findByNumber(challengeDay)
                .orElseThrow(() -> new IllegalArgumentException("해당 Day에 해당하는 챌린지를 찾을 수 없습니다."));

        return challengeConverter.toChallengeResponseDTO(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO.AllChallengeListDto getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();

        return ChallengeResponseDTO.AllChallengeListDto.builder().challengeList(challenges).build();
    }

    @Transactional
    public ChallengeResponseDTO modifyChallenge(Integer challengeDay, ChallengeRequestDTO.ChallengeCreateDto challengeRequestDTO) {
        // 1. 기존 엔티티 조회
        Challenge existingChallenge = challengeRepository.findByNumber(challengeDay)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 챌린지가 존재하지 않습니다."));

        existingChallenge.modifyChallenge(challengeRequestDTO);

        // 3. 변경된 엔티티 저장
        Challenge updatedChallenge = challengeRepository.save(existingChallenge);

        // 4. DTO로 변환 후 반환
        return challengeConverter.toChallengeResponseDTO(updatedChallenge);
    }

    @Transactional
    public ChallengeResponseDTO completeChallenge(Integer challengeDay) {

        Challenge existingChallenge = challengeRepository.findByNumber(challengeDay)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 챌린지가 존재하지 않습니다."));

        existingChallenge.completeChallenge();

        Challenge updatedChallenge = challengeRepository.save(existingChallenge);

        return challengeConverter.toChallengeResponseDTO(updatedChallenge);
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ChallengeSummaryDTO getAllCompletedChallenges() {
        List<Challenge> successChallenges = challengeRepository.findByStatus(Status.SUCCESS);

        List<ChallengeResponseDTO> challengeDTOs = successChallenges.stream()
                .map(ChallengeResponseDTO::new)
                .toList();

        // 전체 갯수와 변환된 Challenge 리스트를 포함하는 DTO 반환
        return ChallengeResponseDTO.ChallengeSummaryDTO.builder()
                .totalCount(challengeDTOs.size()) // 전체 갯수
                .challenges(challengeDTOs) // 변환된 리스트
                .build();
    }
}

