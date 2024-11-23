package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.dto.partleader.request.PartLeaderVoteRequestDto;
import com.example.deple.dto.partleader.response.PartLeaderReadResponseDto;
import com.example.deple.dto.partleader.response.PartLeaderVoteReadResponseDto;
import com.example.deple.service.PartLeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partleader")
@RequiredArgsConstructor
@Slf4j
public class PartLeaderController {
    private final PartLeaderService partLeaderService;

    @GetMapping("/front")
    public ApiResponse<List<PartLeaderReadResponseDto>> findAllFrontPartLeaders() {
        return ApiResponse.successResponse(partLeaderService.findAllFrontPartLeaders());
    }

    @GetMapping("/back")
    public ApiResponse<List<PartLeaderReadResponseDto>> findAllBackPartLeaders() {
        return ApiResponse.successResponse(partLeaderService.findAllBackPartLeaders());
    }

    // 파트장 투표 결과
    @GetMapping("/front/result")
    public ApiResponse<List<PartLeaderVoteReadResponseDto>> findAllFrontPartLeaderVotes() {
        return ApiResponse.successResponse(partLeaderService.findAllFrontPartLeaderVotes());
    }

    @GetMapping("/back/result")
    public ApiResponse<List<PartLeaderVoteReadResponseDto>> findAllBackPartLeaderVotes() {
        return ApiResponse.successResponse(partLeaderService.findAllBackPartLeaderVotes());
    }

    // 파트장 투표
    @PostMapping
    public ApiResponse postPartLeaderVote(@RequestBody PartLeaderVoteRequestDto partLeaderVoteRequestDto, @AuthenticationPrincipal User user) {

        partLeaderService.postPartLeaderVote(partLeaderVoteRequestDto, user);

        return ApiResponse.successWithNoContent();
    }
}
