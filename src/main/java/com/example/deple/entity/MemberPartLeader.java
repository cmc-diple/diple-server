package com.example.deple.entity;

import com.example.deple.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_part_leaders")
public class MemberPartLeader extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "part_leader_id")
    private PartLeader partLeader;

    @Builder
    public MemberPartLeader(Long id, Member member, PartLeader partLeader) {
        this.id = id;
        this.member = member;
        this.partLeader = partLeader;
    }
}
