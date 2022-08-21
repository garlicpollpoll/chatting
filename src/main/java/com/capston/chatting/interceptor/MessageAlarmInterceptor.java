package com.capston.chatting.interceptor;

import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ChatMessageRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ChatMessageService;
import com.capston.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MessageAlarmInterceptor implements HandlerInterceptor {

    private final ChatMessageService chatMessageService;
    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        Integer notReadMessage = 0;

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember != null) {
            List<ChatRoom> findAllMyRooms = chatRoomService.findAllMyRooms(findMember.getId());

            for (ChatRoom findAllMyRoom : findAllMyRooms) {
                notReadMessage += chatMessageService.findByRoomIdAndRead(findAllMyRoom.getRoomId(), "0").size();
            }

            modelAndView.addObject("notReadMessage", notReadMessage);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
