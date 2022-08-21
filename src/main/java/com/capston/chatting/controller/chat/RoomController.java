package com.capston.chatting.controller.chat;

import com.capston.chatting.dto.GetRoomIdDto;
import com.capston.chatting.dto.jwt.LoginInfoDto;
import com.capston.chatting.entity.ChatMessage;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.exception.MemberNotFoundException;
import com.capston.chatting.provider.JwtTokenProvider;
import com.capston.chatting.repository.ChatMessageRepository;
import com.capston.chatting.repository.ItemRepository;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Slf4j
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 로그인 유저 정보 조회하는 API
    @GetMapping("/user")
    @ResponseBody
    public LoginInfoDto getUserInfo(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String name = auth.getName();

        String token = jwtTokenProvider.generateToken(name);

        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("token", token);

        return LoginInfoDto.builder().name(name).token(token).build();
    }

    //자기가 속해있는 roomId 모두 가져오는 API
    @GetMapping("/get_room_id")
    @ResponseBody
    public List<GetRoomIdDto> getRoomId(HttpSession session) {
        log.info("Get Room Id [GET]");
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        List<ChatRoom> allMyRooms = chatRoomService.findAllMyRooms(findMember.getId());

        List<GetRoomIdDto> roomIds = new ArrayList<>();

        for (ChatRoom allMyRoom : allMyRooms) {
            GetRoomIdDto dto = new GetRoomIdDto();
            dto.setRoomId(allMyRoom.getRoomId());
            roomIds.add(dto);
        }

        return roomIds;
    }

    //채팅방 목록 조회
    @GetMapping("/rooms")
    public ModelAndView rooms(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember == null) {
            throw new MemberNotFoundException("해당하는 회원이 없습니다.");
        }

        ModelAndView mv = new ModelAndView("chat_list");

        mv.addObject("list", chatRoomService.findAllMyRooms(findMember.getId()));

        return mv;
    }

    //채팅방 조회
    @GetMapping("/room")
    public ModelAndView room(@RequestParam("roomId") String roomId, HttpSession session, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String token = jwtTokenProvider.generateToken(name);
        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("token", token);

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        List<ChatMessage> findMessages = chatMessageRepository.findChatMessageByRoomId(roomId);

        chatMessageRepository.bulkReadMakeOne(roomId);

        ModelAndView mv = new ModelAndView("room");

        mv.addObject("room", chatRoomService.findRoomById(roomId, findMember));
        mv.addObject("messages", findMessages);

        return mv;
    }

    //채팅방 개설
    @GetMapping("/make_room/{itemId}")
    @Transactional
    public String create(@PathVariable("itemId") Long itemId, HttpSession session, RedirectAttributes redirectAttributes) {
        Item findItem = itemRepository.findById(itemId).orElse(null);

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        ChatRoom chatRoom = chatRoomService.createChatRoom(findMember, findItem);

        redirectAttributes.addFlashAttribute("roomName", chatRoom);

        return "redirect:/chat/rooms";
    }
}
