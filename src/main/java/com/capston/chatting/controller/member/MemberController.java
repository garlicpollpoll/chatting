package com.capston.chatting.controller.member;

import com.capston.chatting.dto.member.CheckNumberDto;
import com.capston.chatting.dto.member.JoinDto;
import com.capston.chatting.dto.member.LoginDto;
import com.capston.chatting.dto.member.SendEmailDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.exception.MemberNotFoundException;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.email.SendEmailService;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;
    private final SendEmailService sendEmailService;

    /**
     * 로그인 페이지 GET 요청.
     * @return 로그인 페이지.
     */
    @GetMapping("/login")
    public String login() {
        log.info("GET /login CheckPoint");
        return "login";
    }

    /**
     * 스프링 시큐리티의 도입으로 더이상 해당 url 로의 요청은 없습니다.
     * @deprecated
     * @param dto
     * @param session
     * @return
     */
    @PostMapping("/login")
    public String loginPost(@ModelAttribute("login")LoginDto dto, HttpSession session) {
        log.info("POST /login CheckPoint");
        Member findMember = memberRepository.findMemberByLoginId(dto.getUsername());

        if (findMember != null) {
            memberService.updateDate(findMember);
        }

        session.setAttribute("loginId", findMember.getLoginId());

        return "redirect:/";
    }

    /**
     * 회원가입 페이지 GET 요청.
     * @return 회원가입 페이지.
     */
    @GetMapping("/join")
    public String join() {
        return "join";
    }

    /**
     * 회원가입 페이지에서 회원가입 버튼을 눌렀을 때 발생하는 컨트롤러.
     * @param dto
     * @return
     */
    @PostMapping("/join")
    public String joinPost(@ModelAttribute("join")JoinDto dto) {
        String encode = encoder.encode(dto.getLoginPw());
        dto.setLoginPw(encode);

        memberService.save(dto);

        return "redirect:/login";
    }

    /**
     * 비활성화 (INACTIVE) 로 바뀐 사용자가 로그인을 하려고 눌렀을 때 넘어오는 페이지.
     * @return 본인인증 페이지로 넘어감.
     */
    @GetMapping("/inactive_account")
    public String inactiveAccount(Model model) {
        CheckNumberDto dto = new CheckNumberDto();

        model.addAttribute("email", dto);

        return "self_authentication/inactive_account";
    }

    /**
     * AJAX 비동기 통신으로 이메일을 보냄.
     * @param dto
     * @param session
     * @return
     */
    @PostMapping("/inactive_account")
    @ResponseBody
    public Map<String, String> inactiveAccountPost(@RequestBody SendEmailDto dto, HttpSession session) {
        sendEmailService.sendMail(dto.getEmail(), session);

        Map<String, String> map = new HashMap<>();
        map.put("message", "이메일이 전송되었습니다.");

        return map;
    }

    /**
     * 이메일로 본인인증 코드를 받아서 본인인증을 직접적으로 해결하는 컨트롤러.
     * 본인인증이 완료되면 Member 의 status 속성이 ACTIVE 로 바뀐다.
     * @param dto
     * @param bindingResult
     * @param session
     * @return
     */
    @PostMapping("/authentication_for_email")
    public String authenticationForEmail(@Validated @ModelAttribute("email")CheckNumberDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "self_authentication/inactive_account";
        }

        boolean isAuthentication = sendEmailService.checkNumber(dto, session);

        if (!isAuthentication) {
            bindingResult.reject("NotAuthentication");
            return "self_authentication/inactive_account";
        }

        return "redirect:/";
    }
}
