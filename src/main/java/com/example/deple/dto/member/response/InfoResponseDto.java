package com.example.deple.dto.member.response;

import com.example.deple.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoResponseDto {
	private Long id;
	private String username;
	private String name;
	private String email;
	private String teamName;
	private String part;
	private Boolean isVerified;
	private Boolean isProjectVoted;
	private Boolean isPartLeaderVoted;

	public static InfoResponseDto from(Member member) {
		return InfoResponseDto.builder()
				.id(member.getId())
				.username(member.getUsername())
				.name(member.getName())
				.email(member.getEmail())
				.teamName(member.getTeamName().toString())
				.part(member.getPart().toString())
				.isVerified(member.getIsVerified())
				.isProjectVoted(member.getIsProjectVoted())
				.isPartLeaderVoted(member.getIsPartLeaderVoted())
				.build();
	}
}
