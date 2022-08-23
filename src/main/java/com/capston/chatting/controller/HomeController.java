package com.capston.chatting.controller;

import com.capston.chatting.config.batch.InactiveMemberJobConfig;
import com.capston.chatting.dto.GetRoomIdDto;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.MemberRepository;
import com.capston.chatting.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    // Test
    private final JobLauncher jobLauncher;
    private final InactiveMemberJobConfig config;

    @GetMapping("/job_start")
    @ResponseBody
    public String jobStart() {
        JobExecution run = null;
        try {
            run = jobLauncher.run(config.inactiveMemberJob(), new JobParameters());
            log.info("IsRunning : {}", run.isRunning());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return String.valueOf(run.getJobInstance());
    }

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
