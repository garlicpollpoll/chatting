package com.capston.chatting.principal;

import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.otp.TOTPTokenGenerator;
import com.capston.chatting.service.otp.TOTPTokenValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findMemberByLoginId(username);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletResponse response = attr.getResponse();

        if (findMember != null) {
            if (findMember.getStatus() == MemberStatus.INACTIVE) {
                try {
                    response.sendRedirect("/inactive_account");
                } catch (IOException e) {
                    log.error("[ERROR] In Send Redirect, Have a Problem");
                    throw new RuntimeException(e);
                }
            }
        }

        HttpSession session = attr.getRequest().getSession();
        session.setAttribute("loginId", username);
        return new PrincipalDetail(findMember);
    }
}
