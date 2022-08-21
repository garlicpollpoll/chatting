package com.capston.chatting.service;

import com.capston.chatting.dto.member.JoinDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberRole;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(JoinDto dto) {
        Member member = new Member(dto.getLoginId(), dto.getLoginPw(), dto.getName(), 0, MemberRole.ROLE_USER);

        memberRepository.save(member);
    }
}
