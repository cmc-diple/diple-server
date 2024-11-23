package com.example.deple.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailResponseDto {
    private String code;
}
