package com.example.deple.security.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfig {
    public static final String CHAT_MODEL = "gpt-4o-mini";
    public static final String SYSTEM_ROLE = "system";
    public static final String USER_ROLE = "user";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";

}
