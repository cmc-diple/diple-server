package com.example.deple.dto.auth.request;

import com.example.deple.entity.Member;
import com.example.deple.entity.enums.Part;
import com.example.deple.entity.enums.Team;
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
    private Team teamName;
    private Part part;
    private Boolean isVerified;

    public Member of(PasswordEncoder passwordEncoder)  {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .teamName(teamName)
                .part(part)
                .isVerified(isVerified)
                .build();
    }
}