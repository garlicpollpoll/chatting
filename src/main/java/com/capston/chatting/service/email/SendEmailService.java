package com.capston.chatting.service.email;

import com.capston.chatting.dto.member.CheckNumberDto;
import com.capston.chatting.dto.member.SendEmailDto;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;

    public void sendMail(String myEmail, HttpSession session) {
        int num1 = (int) (Math.random() * 10);
        int num2 = (int) (Math.random() * 10);
        int num3 = (int) (Math.random() * 10);
        int num4 = (int) (Math.random() * 10);
        int num5 = (int) (Math.random() * 10);
        int num6 = (int) (Math.random() * 10);

        String checkNum = String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3) + String.valueOf(num4) + String.valueOf(num5) + String.valueOf(num6);

        List<String> toUserList = new ArrayList<>();

        toUserList.add(myEmail);

        int toUserSize = toUserList.size();

        SimpleMailMessage simpleMessage = new SimpleMailMessage();

        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        simpleMessage.setSubject("본인인증");

        simpleMessage.setText(checkNum);

        javaMailSender.send(simpleMessage);

        session.setAttribute("checkNumber", checkNum);
    }

    @Transactional
    public boolean checkNumber(CheckNumberDto dto, HttpSession session) {
        String checkNumber = (String) session.getAttribute("checkNumber");

        if (dto.getCheckNum().equals(checkNumber)) {
            String loginId = (String) session.getAttribute("loginId");

            Member findMember = memberRepository.findMemberByLoginId(loginId);
            findMember.setActive();
            return true;
        }
        else {
            return false;
        }
    }
}
