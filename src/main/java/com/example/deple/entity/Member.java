package com.example.deple.entity;

import com.example.deple.dto.auth.request.AuthRequestDto;
import com.example.deple.entity.base.BaseTimeEntity;
import com.example.deple.entity.enums.Part;
import com.example.deple.entity.enums.Team;
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

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberPartLeader> memberPartLeaders = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team teamName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Part part;

    @Column(nullable = false)
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean isProjectVoted;

    @Column(nullable = false)
    private Boolean isPartLeaderVoted;

    @Builder
    public Member(String username, String password, String name,
                  String email, Team teamName, Part part, Boolean isVerified) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.teamName = teamName;
        this.part = part;
        this.isVerified = isVerified;
        this.isProjectVoted = Boolean.FALSE;
        this.isPartLeaderVoted = Boolean.FALSE;
    }

    public void updateIsVerified() {
        this.isVerified = Boolean.TRUE;
    }

    public void updateIsProjectVoted() {
        this.isProjectVoted = Boolean.TRUE;
    }

    public void updateIsPartLeaderVoted() {
        this.isPartLeaderVoted = Boolean.TRUE;
    }

    public static Member of(AuthRequestDto authRequestDto) {
        return Member.builder()
                .username(authRequestDto.getUsername())
                .password(authRequestDto.getPassword())
                .name(authRequestDto.getName())
                .email(authRequestDto.getEmail())
                .teamName(authRequestDto.getTeamName())
                .part(authRequestDto.getPart())
                .isVerified(authRequestDto.getIsVerified())
                .build();
    }

}
