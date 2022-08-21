package com.capston.chatting.service;

import com.capston.chatting.dto.ChatRoomDTO;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.entity.Item;
import com.capston.chatting.entity.Member;
import com.capston.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAllMyRooms(Long memberId) {
        List<ChatRoom> result = chatRoomRepository.findMyRoom(memberId);

        return result;
    }

    public ChatRoom findRoomById(String id, Member member) {
        return chatRoomRepository.findByRoomId(id, member.getId());
    }

    public ChatRoom findRoomByMemberAndItem(Member member, Item item) {
        return chatRoomRepository.findByMemberAndItem(member.getId(), item.getId());
    }

    // 채팅방을 개설하면 상대방도 같은 채팅방을 만들어줌
    public ChatRoom createChatRoom(Member member, Item item) {
        String roomId = makeRoomId();
        ChatRoom room = new ChatRoom(roomId, member, item);
        ChatRoom roomItemOwner = new ChatRoom(roomId, item.getMember(), item);

        ChatRoom findChatRoom = findRoomByMemberAndItem(member, item);

        if (findChatRoom == null) {
            chatRoomRepository.save(room);
            chatRoomRepository.save(roomItemOwner);
        }

        return room;
    }

    private String makeRoomId() {
        return UUID.randomUUID().toString();
    }
}
