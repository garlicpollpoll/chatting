package com.capston.chatting.controller.chat;

import com.capston.chatting.dto.ChatMessageDTO;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Member;
import com.capston.chatting.provider.JwtTokenProvider;
import com.capston.chatting.repository.ChatMessageRepository;
import com.capston.chatting.repository.ChatRoomRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ChatMessageService;
import com.capston.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;
    private final ChatMessageService chatMessageService;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message) {
        Member findMember = memberRepository.findMemberByLoginId(message.getWriter());

        ChatRoom findRoom = chatRoomService.findRoomById(message.getRoomId(), findMember);

        String date = LocalDateTime.now().toString().substring(11, 16);

        chatMessageService.save(findRoom, message.getMessage(), date, findMember.getLoginId());

        log.info("{} : 메시지가 입력되었습니다. message = {}, writer = {}", message.getRoomId(), message.getMessage(), message.getWriter());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

//    @MessageMapping(value = "/chat/enter")
//    public void enter(ChatMessageDTO chatMessage) {
//        chatMessage.setMessage(chatMessage.getWriter() + "님이 채팅방에 참여하셨습니다.");
//        template.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
//    }
}
