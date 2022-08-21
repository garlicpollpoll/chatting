package com.capston.chatting.controller.member;

import com.capston.chatting.dto.member.JoinDto;
import com.capston.chatting.dto.member.LoginDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.exception.MemberNotFoundException;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/login")
    public String login() {
        log.info("GET /login CheckPoint");
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("login")LoginDto dto, HttpSession session) {
        log.info("POST /login CheckPoint");
        Member findMember = memberRepository.findMemberByLoginId(dto.getUsername());

        if (findMember == null) {
            throw new MemberNotFoundException("해당하는 회원이 없습니다.");
        }

        session.setAttribute("loginId", findMember.getLoginId());

        return "redirect:/";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String joinPost(@ModelAttribute("join")JoinDto dto) {
        String encode = encoder.encode(dto.getLoginPw());
        dto.setLoginPw(encode);

        memberService.save(dto);

        return "redirect:/login";
    }
}
