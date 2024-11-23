package com.example.deple.service;


import com.example.deple.security.config.GptConfig;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GptService {
    private final RestTemplate restTemplate;

    @Value("${chatgpt.api-key}")
    private String API_KEY;

    @Transactional
    public String getGptResponse(String prompt) {
        log.info("GPT API 호출 시작 - prompt: {}", prompt);

        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            // 요청 바디 구성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", GptConfig.CHAT_MODEL);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                    "role", GptConfig.SYSTEM_ROLE,
                    "content", "You are a helpful assistant."
            ));
            messages.add(Map.of(
                    "role", GptConfig.USER_ROLE,
                    "content", prompt
            ));

            requestBody.put("messages", messages);

            // HTTP 엔티티 생성
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // API 요청 보내기
            ResponseEntity<Map> response = restTemplate.exchange(
                    GptConfig.CHAT_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 응답 처리
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
                    String content = message.get("content");
                    log.info("GPT API 호출 성공 - response: {}", content);
                    return content;
                }
            }

            log.warn("GPT API 응답이 비어있습니다.");
            return "No response from GPT";

        } catch (Exception e) {
            log.error("GPT API 호출 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("GPT API 호출 실패", e);
        }
    }



}
