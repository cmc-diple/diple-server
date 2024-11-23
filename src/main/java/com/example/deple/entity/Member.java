package com.example.deple.entity;

import com.example.deple.dto.auth.request.AuthRequestDto;
import com.example.deple.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean isVerified;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChallenge> memberChallenge = new ArrayList<>();


    @Builder
    public Member(String username, String password, String name,
                  String email, Boolean isVerified) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isVerified = isVerified;
    }

    public void updateIsVerified() {
        this.isVerified = Boolean.TRUE;
    }

    public static Member of(AuthRequestDto authRequestDto) {
        return Member.builder()
                .username(authRequestDto.getUsername())
                .password(authRequestDto.getPassword())
                .name(authRequestDto.getName())
                .email(authRequestDto.getEmail())
                .isVerified(authRequestDto.getIsVerified())
                .build();
    }

}
