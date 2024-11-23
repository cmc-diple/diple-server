package com.example.deple.service;

import com.example.deple.dto.auth.request.AuthRequestDto;
import com.example.deple.dto.auth.request.EmailMessage;
import com.example.deple.dto.auth.request.SigninRequestDto;
import com.example.deple.dto.auth.request.UsernameRequestDto;
import com.example.deple.dto.auth.response.EmailResponseDto;
import com.example.deple.dto.security.TokenDto;
import com.example.deple.entity.Member;
import com.example.deple.repository.MemberRepository;
import com.example.deple.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final JavaMailSender emailSender;

    @Transactional
    public TokenDto signup(AuthRequestDto authRequestDto) {
        Optional<Member> findUser = memberRepository.findByUsername(authRequestDto.getUsername());
        if (findUser.isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = authRequestDto.of(passwordEncoder);

        memberRepository.save(member);

        SigninRequestDto signinRequestDto = SigninRequestDto.builder()
                .username(authRequestDto.getUsername())
                .password(authRequestDto.getPassword())
                .build();

        return signin(signinRequestDto);
    }

    @Transactional
    public TokenDto signin(SigninRequestDto signinRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = signinRequestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        // authentication 로부터 username을 가져와서 이메일 인증 여부 확인
        // 이메일 인증이 완료되지 않은 유저는 로그인 불가
        Optional<Member> findUser = memberRepository.findByUsername(authentication.getName());
        if (findUser.isPresent() && !findUser.get().getIsVerified()) {
            throw new RuntimeException("이메일 인증이 완료되지 않은 유저입니다.");
        }

        return tokenProvider.createAccessToken(authentication);
    }

    public void checkDuplicatedUsername(UsernameRequestDto usernameRequestDto) {
        String username = usernameRequestDto.getUsername();
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isPresent()) {
            log.debug("AuthService.checkDuplicatedUsername exception occur username: {}", username);
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }
    }

    public EmailResponseDto sendEmail(EmailMessage emailMessage) {
        checkDuplicatedEmail(emailMessage.getTo());
        String code = createCode();
        SimpleMailMessage emailForm = createEmailForm(emailMessage, code);
        try {
            emailSender.send(emailForm);
            log.info("AuthService.sendEmail Success 200");
            return EmailResponseDto.builder().code(code).build();
        } catch (RuntimeException e) {
            log.debug("AuthService.sendEmail exception to: {}, subject: {}, text: {}", emailMessage.getTo(), emailMessage.getSubject(), code);
            throw new RuntimeException("이메일을 보낼 수 없습니다.");
        }
    }

    private String createCode() {
        int lenth = 6;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lenth; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: builder.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: builder.append((char) ((int) random.nextInt(26) + 65)); break;
                default: builder.append(random.nextInt(9));
            }
        }
        return builder.toString();
    }

    private SimpleMailMessage createEmailForm(EmailMessage emailMessage, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(text);

        return message;
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("AuthService.checkDuplicatedEmail exception occur email: {}", email);
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
    }
}
