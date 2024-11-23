package com.example.deple.entity;

import com.example.deple.entity.base.BaseTimeEntity;
import com.example.deple.entity.enums.Part;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "part_leaders")
public class PartLeader extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "partLeader")
    private List<MemberPartLeader> memberPartLeaders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Part part;

    @Builder
    public PartLeader(Long id, Project projectId, String name, int count, Part part) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.count = count;
        this.part = part;
    }

    public void updateCount() {
        this.count += 1;
    }
}
