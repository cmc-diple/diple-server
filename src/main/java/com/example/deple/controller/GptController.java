package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.service.GptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
@Slf4j
public class GptController {
    private final GptService gptService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<?>> chatWithGpt() {
        try {
            // 테스트용 더미 프롬프트
            String testPrompt = "Hello, this is a test message. Please respond with a simple greeting.";
            log.info("GPT 테스트 요청 시작 - test prompt: {}", testPrompt);

            // 실제 요청 대신 테스트 프롬프트 사용
            String response = gptService.getGptResponse(testPrompt);

            log.info("GPT 응답 수신: {}", response);
            return ResponseEntity.ok(ApiResponse.successResponse(response));
        } catch (Exception e) {
            log.error("GPT 채팅 요청 처리 중 에러 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.errorResponse(e.getMessage()));
        }
    }
}
