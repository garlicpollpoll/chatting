package com.capston.chatting.config;

import com.capston.chatting.interceptor.CheckOtpApplyInterceptor;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.chat.ChatMessageService;
import com.capston.chatting.service.chat.ChatRoomService;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckOtpApplyInterceptor(memberService))
                .order(1)
                .addPathPatterns("/chat/**", "/upload")
                .excludePathPatterns();

    }
}
