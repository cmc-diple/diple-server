package com.example.deple.service;

import com.example.deple.dto.project.request.ProjectVoteRequestDto;
import com.example.deple.dto.project.response.ProjectReadResponseDto;
import com.example.deple.dto.project.response.ProjectVoteReadResponseDto;
import com.example.deple.entity.Member;
import com.example.deple.entity.MemberProject;
import com.example.deple.entity.Project;
import com.example.deple.repository.MemberProjectRepository;
import com.example.deple.repository.MemberRepository;
import com.example.deple.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

    public List<ProjectReadResponseDto> findAllProjects() {
        List<Project> projectList = projectRepository.findAll();
        List<ProjectReadResponseDto> projectReadResponseList = projectList.stream()
                .map(project -> ProjectReadResponseDto.from(project))
                .collect(Collectors.toList());
        return projectReadResponseList;
    }

    public List<ProjectVoteReadResponseDto> findAllProjectVotes() {
        List<Project> projectList = projectRepository.findAllByOrderByCountDesc();
        List<ProjectVoteReadResponseDto> projectVoteReadResponses = projectList.stream()
                .map(project -> ProjectVoteReadResponseDto.from(project))
                .collect(Collectors.toList());
        return projectVoteReadResponses;
    }

    @Transactional
    public void postProjectVote(ProjectVoteRequestDto projectVoteRequest, User user) {
        Member member = memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        if (!member.getIsProjectVoted()) {
            Project project = projectRepository.findById(projectVoteRequest.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 없습니다."));

            project.updateCount();
            member.updateIsProjectVoted();

            memberProjectRepository.save(MemberProject.builder()
                            .member(member)
                            .project(project)
                            .build());
            projectRepository.save(project);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("이미 투표를 했습니다");
        }

        return;
    }
}
