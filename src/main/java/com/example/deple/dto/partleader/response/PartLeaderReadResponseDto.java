package com.example.deple.dto.partleader.response;

import com.example.deple.entity.PartLeader;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartLeaderReadResponseDto {
    private Long id;
    private String name;
    private String projectName;

    public static PartLeaderReadResponseDto from(PartLeader partLeader) {
        return PartLeaderReadResponseDto.builder()
                .id(partLeader.getId())
                .name(partLeader.getName())
                .projectName(partLeader.getProjectId().getName())
                .build();
    }
}
