package com.example.deple.service;

import com.example.deple.dto.partleader.request.PartLeaderVoteRequestDto;
import com.example.deple.dto.partleader.response.PartLeaderReadResponseDto;
import com.example.deple.dto.partleader.response.PartLeaderVoteReadResponseDto;
import com.example.deple.entity.*;
import com.example.deple.entity.enums.Part;
import com.example.deple.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PartLeaderService {
    private final PartLeaderRepository partLeaderRepository;
    private final MemberRepository memberRepository;
    private final MemberPartLeaderRepository memberPartLeaderRepository;

    public List<PartLeaderReadResponseDto> findAllFrontPartLeaders() {
        List<PartLeader> partLeaderList = partLeaderRepository.findAllByPart(Part.FRONTEND);
        List<PartLeaderReadResponseDto> partLeaderReadResponseDtoList = partLeaderList.stream()
                .map(partLeader -> PartLeaderReadResponseDto.from(partLeader))
                .collect(Collectors.toList());
        return partLeaderReadResponseDtoList;
    }

    public List<PartLeaderReadResponseDto> findAllBackPartLeaders() {
        List<PartLeader> partLeaderList = partLeaderRepository.findAllByPart(Part.BACKEND);
        List<PartLeaderReadResponseDto> partLeaderReadResponseDtoList = partLeaderList.stream()
                .map(partLeader -> PartLeaderReadResponseDto.from(partLeader))
                .collect(Collectors.toList());
        return partLeaderReadResponseDtoList;
    }

    public List<PartLeaderVoteReadResponseDto> findAllFrontPartLeaderVotes() {
        List<PartLeader> partLeaderList = partLeaderRepository.findAllByPartOrderByCountDesc(Part.FRONTEND);
        List<PartLeaderVoteReadResponseDto> partLeaderVoteReadResponseDtoList = partLeaderList.stream()
                .map(partLeader -> PartLeaderVoteReadResponseDto.from(partLeader))
                .collect(Collectors.toList());
        return partLeaderVoteReadResponseDtoList;
    }

    public List<PartLeaderVoteReadResponseDto> findAllBackPartLeaderVotes() {
        List<PartLeader> partLeaderList = partLeaderRepository.findAllByPartOrderByCountDesc(Part.BACKEND);
        List<PartLeaderVoteReadResponseDto> partLeaderVoteReadResponseDtoList = partLeaderList.stream()
                .map(partLeader -> PartLeaderVoteReadResponseDto.from(partLeader))
                .collect(Collectors.toList());
        return partLeaderVoteReadResponseDtoList;
    }

    @Transactional
    public void postPartLeaderVote(PartLeaderVoteRequestDto partLeaderVoteRequestDto, User user) {
        Member member = memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 없습니다."));

        if (!member.getIsPartLeaderVoted()) {
            PartLeader partLeader = partLeaderRepository.findById(partLeaderVoteRequestDto.getPartLeaderId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 파트장은 존재하지 않는 사람입니다."));

            if (member.getPart() != partLeader.getPart()) {
                throw new IllegalArgumentException("자신의 파트에만 투표할 수 있습니다.");
            }

            partLeader.updateCount();
            member.updateIsPartLeaderVoted();

            memberPartLeaderRepository.save(MemberPartLeader.builder()
                        .member(member)
                        .partLeader(partLeader)
                        .build());
            partLeaderRepository.save(partLeader);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("이미 투표를 했습니다");
        }

        return;
    }
}
