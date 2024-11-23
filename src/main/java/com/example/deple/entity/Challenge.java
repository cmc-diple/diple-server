package com.example.deple.entity;

import com.example.deple.entity.base.BaseTimeEntity;
import com.example.deple.entity.enums.Status;
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
@Table(name = "Challenge")
public class Challenge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String details;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<MemberChallenge> memberChallenge = new ArrayList<>();

    @Builder
    public Challenge(Integer number, String title, String details, List<MemberChallenge> memberChallenge) {
        this.number = number;
        this.title = title;
        this.details = details;
        this.memberChallenge = memberChallenge;
    }
}
