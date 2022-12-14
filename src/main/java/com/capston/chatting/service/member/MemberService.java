package com.capston.chatting.service.member;

import com.capston.chatting.dto.member.JoinDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberRole;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.enums.OTPApplyStatus;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(JoinDto dto) {
        Member member = new Member(dto.getLoginId(), dto.getLoginPw(), dto.getName(), 0, MemberRole.ROLE_USER, MemberStatus.ACTIVE, LocalDateTime.now(), OTPApplyStatus.CANCEL);

        memberRepository.save(member);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findMemberByLoginId(loginId);
    }

    public void updateDate(Member member) {
        member.setUpdateDate(LocalDateTime.now());
    }

    public void applyOtp(Member member) {
        member.setOtpApply();
    }
}
