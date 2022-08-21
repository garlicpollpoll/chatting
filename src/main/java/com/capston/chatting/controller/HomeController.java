package com.capston.chatting.controller;

import com.capston.chatting.dto.GetRoomIdDto;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ItemRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId != null) {
            Member findMember = memberRepository.findMemberByLoginId(loginId);
            List<ChatRoom> allMyRooms = chatRoomService.findAllMyRooms(findMember.getId());
            List<GetRoomIdDto> getRoomIdDto = new ArrayList<>();
            for (ChatRoom allMyRoom : allMyRooms) {
                GetRoomIdDto dto = new GetRoomIdDto();
                dto.setRoomId(allMyRoom.getRoomId());
                dto.setItemName(allMyRoom.getItem().getItemName());
                getRoomIdDto.add(dto);
            }

            model.addAttribute("myRoom", getRoomIdDto);
        }
        return "home";
    }
}
