package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.dto.challenge.ChallengeRequestDTO;
import com.example.deple.dto.challenge.ChallengeResponseDTO;
import com.example.deple.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping({"/challenges/{challengeDay}"})
    @Operation(summary="챌린지 생성 API")
    public ApiResponse<ChallengeResponseDTO> challenge(
            @RequestBody ChallengeRequestDTO.ChallengeCreateDto request,
            @PathVariable("challengeDay") Integer challengeDay) {
        ChallengeResponseDTO challengeResponseDTO = challengeService.createChallenge(challengeDay,request);
        return ApiResponse.successResponse(challengeResponseDTO);
    }

    @GetMapping({"/challenges/{challengeDay}"})
    @Operation(summary="챌린지 조회 API")
    public ApiResponse<ChallengeResponseDTO> challenge(
            @PathVariable("challengeDay") Integer challengeDay) {
        ChallengeResponseDTO challengeResponseDTO = challengeService.getChallenge(challengeDay);
        return ApiResponse.successResponse(challengeResponseDTO);
    }
}
