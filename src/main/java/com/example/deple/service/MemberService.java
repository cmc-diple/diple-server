package com.example.deple.service;

import com.example.deple.dto.member.response.InfoResponseDto;
import com.example.deple.entity.Member;
import com.example.deple.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public InfoResponseDto getInfoByUsername(String username) {
		Optional<Member> findMember= memberRepository.findByUsername(username);
		if (findMember.isEmpty()) {
			throw new RuntimeException("존재하지 않는 유저입니다");
		}
		Member member = findMember.get();

		return InfoResponseDto.from(member);
	}
}
