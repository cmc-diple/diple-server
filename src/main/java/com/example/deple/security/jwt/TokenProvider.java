package com.example.deple.security.jwt;

import com.example.deple.dto.security.TokenDto;
import com.example.deple.entity.RefreshToken;
import com.example.deple.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 242 * 60 * 60 * 1000L;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.secret}")
    private String secret;
    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        Base64.Decoder decoders = Base64.getDecoder();
        byte[] keyBytes = decoders.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(String nickname, String authorities, String type) {
        long now = (new Date()).getTime();
        long time = type.equals("access") ? ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPIRE_TIME;
        Date tokenExpiredIn = new Date(now + time);

        String token = Jwts.builder()
                .setSubject(nickname)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(tokenExpiredIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    public TokenDto createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createToken(authentication.getName(), authorities, "access");
        String newRefreshToken = createToken(authentication.getName(), authorities, "refresh");

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUserName(authentication.getName());

        if (oldRefreshToken.isPresent()) {
            refreshTokenRepository.save(oldRefreshToken.get().updateToken(newRefreshToken));
        } else {
            RefreshToken refreshToken = RefreshToken.builder()
                    .userName(authentication.getName())
                    .refreshToken(newRefreshToken)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expried JWT Token");
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            log.error("Jwt 토큰이 잘못되었습니다");
        }
        return false;
    }

    public boolean refreshTokenValidation(String token) {
        if (!validateAccessToken(token)) return false;

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserName(getEmailFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
