package com.capston.chatting.interceptor;

import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.OTPApplyStatus;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class CheckOtpApplyInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        String isAuthentication = (String) session.getAttribute("isAuthentication");
        Member findMember = null;

        if (loginId != null) {
            findMember = memberService.findByLoginId(loginId);
        }

        if (findMember.getOtpStatus() == OTPApplyStatus.APPLY) {
            if (isAuthentication != "OK") {
                response.sendRedirect("/2FA_authentication");
            }
        }
    }
}
