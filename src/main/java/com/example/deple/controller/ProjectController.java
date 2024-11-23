package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.dto.project.request.ProjectVoteRequestDto;
import com.example.deple.dto.project.response.ProjectReadResponseDto;
import com.example.deple.dto.project.response.ProjectVoteReadResponseDto;
import com.example.deple.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ApiResponse<List<ProjectReadResponseDto>> findAllProjects() {
        return ApiResponse.successResponse(projectService.findAllProjects());
    }

    // 데모데이 결과
    @GetMapping("/result")
    public ApiResponse<List<ProjectVoteReadResponseDto>> findAllProjectVotes() {
        return ApiResponse.successResponse(projectService.findAllProjectVotes());
    }

    // 데모데이 투표
    @PostMapping
    public ApiResponse postProjectVote(@RequestBody ProjectVoteRequestDto projectVoteRequest, @AuthenticationPrincipal User user) {

        projectService.postProjectVote(projectVoteRequest, user);

        return ApiResponse.successWithNoContent();
    }

}
