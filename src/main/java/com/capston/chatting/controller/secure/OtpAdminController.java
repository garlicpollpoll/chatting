package com.capston.chatting.controller.secure;

import com.capston.chatting.dto.member.CheckNumberDto;
import com.capston.chatting.dto.member.SendEmailDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.service.email.SendEmailService;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OtpAdminController {

    private final MemberService memberService;
    private final SendEmailService sendEmailService;

    @GetMapping("/secure_upgrade")
    public String upgrade() {
        return "secure/secure_upgrade";
    }

    @GetMapping("/secure/OTP_apply")
    @Transactional
    public String OtpApply(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberService.findByLoginId(loginId);

        memberService.applyOtp(findMember);

        session.setAttribute("isAuthentication", "OK");

        return "home";
    }

    @PostMapping("/send_mail/otp_cancel")
    @ResponseBody
    public Map<String, String> sendMailOtpCancel(@RequestBody SendEmailDto dto, HttpSession session) {
        sendEmailService.sendMail(dto.getEmail(), session);

        Map<String, String> map = new HashMap<>();
        map.put("message", "메일이 전송되었습니다.");

        return map;
    }

    @GetMapping("/secure/OTP_cancel")
    public String secureOtpCancel(Model model) {
        CheckNumberDto dto = new CheckNumberDto();

        model.addAttribute("check", dto);

        return "secure/secure_otp_cancel";
    }

    @PostMapping("/secure/otp_cancel")
    @Transactional
    public String secureOtpCancelPost(@Validated @ModelAttribute("check")CheckNumberDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "secure/secure_otp_cancel";
        }

        boolean isAuthentication = sendEmailService.checkNumber(dto, session);

        if (!isAuthentication) {
            bindingResult.reject("NotAuthentication");
            return "secure/secure_otp_cancel";
        }

        String loginId = (String) session.getAttribute("loginId");
        if (loginId != null) {
            Member findMember = memberService.findByLoginId(loginId);
            findMember.setOtpCancel();
        }

        session.removeAttribute("isAuthentication");

        return "redirect:/";
    }
}
