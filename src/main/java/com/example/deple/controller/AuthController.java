package com.example.deple.controller;

import com.example.deple.common.ApiResponse;
import com.example.deple.dto.auth.request.*;
import com.example.deple.dto.auth.response.EmailResponseDto;
import com.example.deple.dto.security.TokenDto;
import com.example.deple.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<TokenDto> signup(@RequestBody AuthRequestDto authRequestDto) {
        log.info("유저 회원가입하기");
        return ApiResponse.successResponse(authService.signup(authRequestDto));
    }

    @PostMapping("/signin")
    public ApiResponse<TokenDto> signin(@RequestBody SigninRequestDto signinRequestDto) {
        log.info("유저 로그인하기");
        return ApiResponse.successResponse(authService.signin(signinRequestDto));
    }

    @PostMapping("/verify/username")
    public ApiResponse<?> checkUsername(@RequestBody UsernameRequestDto usernameRequestDto) {
        log.info("아이디 중복 체크");
        authService.checkDuplicatedUsername(usernameRequestDto);
        return ApiResponse.successWithNoContent();
    }

    @PostMapping("/verify/email")
    public ApiResponse<EmailResponseDto> sendMessage(@RequestBody EmailRequestDto emailRequestDto) {
        log.info("이메일 인증");

        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailRequestDto.getEmail())
                .subject("[REDDI] 이메일 인증을 위한 인증 코드 발송")
                .build();

        return ApiResponse.successResponse(authService.sendEmail(emailMessage));
    }
}