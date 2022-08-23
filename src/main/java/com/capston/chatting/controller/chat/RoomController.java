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
import com.capston.chatting.service.chat.ChatRoomService;
import com.capston.chatting.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MemberService memberService;


    /**
     * 로그인 유저 정보 조회하는 API 해당 url 로 접근할 경우 jwt token 을 받는다.
     * @param response
     * @return
     */
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

    /**
     * axios 로 해당 url 로의 접근은 이제 하지 않습니다.
     * @deprecated
     * @param session
     * @return
     */
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

    /**
     * 채팅방 목록 조회
     * @param session
     * @return
     */
    @GetMapping("/rooms")
    public ModelAndView rooms(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember == null) {
            throw new MemberNotFoundException("해당하는 회원이 없습니다.");
        }
        else {
            memberService.updateDate(findMember);
        }

        ModelAndView mv = new ModelAndView("chat_list");

        mv.addObject("list", chatRoomService.findAllMyRooms(findMember.getId()));

        return mv;
    }

    /**
     * 채팅방 안에 있는 대화 내용 조회
     * @param roomId
     * @param session
     * @param response
     * @return
     */
    @GetMapping("/room")
    public ModelAndView room(@RequestParam("roomId") String roomId, HttpSession session, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String token = jwtTokenProvider.generateToken(name);
        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("token", token);

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember != null) {
            memberService.updateDate(findMember);
        }

        List<ChatMessage> findMessages = chatMessageRepository.findChatMessageByRoomId(roomId);

        chatMessageRepository.bulkReadMakeOne(roomId);

        ModelAndView mv = new ModelAndView("room");

        mv.addObject("room", chatRoomService.findRoomById(roomId, findMember));
        mv.addObject("messages", findMessages);

        return mv;
    }

    /**
     * itemId 를 입력받아 채팅방 개설
     * @param itemId
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/make_room/{itemId}")
    @Transactional
    public String create(@PathVariable("itemId") Long itemId, HttpSession session, RedirectAttributes redirectAttributes) {
        Item findItem = itemRepository.findById(itemId).orElse(null);

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberRepository.findMemberByLoginId(loginId);

        if (findMember != null) {
            memberService.updateDate(findMember);
        }

        ChatRoom chatRoom = chatRoomService.createChatRoom(findMember, findItem);

        redirectAttributes.addFlashAttribute("roomName", chatRoom);

        return "redirect:/chat/rooms";
    }
}
