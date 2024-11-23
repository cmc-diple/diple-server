package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.entity.Challenge;
import com.example.deple.repository.ChallengeRepository;
import com.example.deple.service.GptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
@Slf4j
public class GptController {
    private final GptService gptService;
    private final ChallengeRepository challengeRepository;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<?>> chatWithGpt() {
        try {
            List<Challenge> challenges = challengeRepository.findAll();

            // challenges 리스트를 순회하면서 각 challenge의 title 과 detail 을 문자열로 만듦
            // <목표제목> : [title]
            // <목표내용> : [detail]
            StringBuilder sb = new StringBuilder();
            for (Challenge challenge : challenges) {
                sb.append("<목표제목> : ");
                sb.append(challenge.getTitle()).append(", ");
                sb.append("<목표내용> : ");
                sb.append(challenge.getDetails());
                sb.append("\n");
            }

            String systemPrompt = """
                    당신은 유저가 12월1일부터 12월31일까지 1달간 매일 목표를 세우고 이룬 목표에 대해 유저를 칭찬해주고 요약해주는 역할입니다.
                    유저가 세운 목표와 내용에 대해 요약해주고 상냥하게 칭찬해주세요.
                    단, 결과는 2문장 이내로 작성해주세요.
                    """;

            String response = gptService.getGptResponse(sb.toString(), systemPrompt);
            return ResponseEntity.ok(ApiResponse.successResponse(response));
        } catch (Exception e) {
            log.error("GPT 채팅 요청 처리 중 에러 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.errorResponse(e.getMessage()));
        }
    }
}
