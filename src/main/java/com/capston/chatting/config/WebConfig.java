package com.capston.chatting.config;

import com.capston.chatting.interceptor.MessageAlarmInterceptor;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ChatMessageService;
import com.capston.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new MessageAlarmInterceptor(chatMessageService, memberRepository, chatRoomService))
//                .order(1)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/*.ico", "/error", "/auth/loginProc", "/chat/**");
//
//    }
}
