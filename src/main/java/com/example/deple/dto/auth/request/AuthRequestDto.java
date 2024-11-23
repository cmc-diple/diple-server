package com.example.deple.dto.auth.request;

import com.example.deple.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
public class AuthRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private Boolean isVerified;

    public Member of(PasswordEncoder passwordEncoder)  {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .isVerified(isVerified)
                .build();
    }
}