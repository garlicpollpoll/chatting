package com.capston.chatting.config;

import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.OTPApplyStatus;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberService.findByLoginId(loginId);
            if (findMember.getOtpStatus() == OTPApplyStatus.APPLY) {
                setDefaultTargetUrl("/2FA_authentication");
            }
            else {
                setDefaultTargetUrl("/");
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
