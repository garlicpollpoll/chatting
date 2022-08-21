package com.capston.chatting.service;

import com.capston.chatting.entity.ChatMessage;
import com.capston.chatting.entity.ChatRoom;
import com.capston.chatting.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void save(ChatRoom room, String message, String writeTime, String writer) {
        String timeFormat;

        String hours = writeTime.substring(0, 2);

        if (Integer.parseInt(hours) <= 11) {
            timeFormat = "오전 " + writeTime;
        }
        else {
            timeFormat = "오후 " + (Integer.parseInt(hours) - 12) + writeTime.substring(2, 5);
        }

        ChatMessage msg = new ChatMessage(room, room.getRoomId(), message, timeFormat, writer, "0");

        chatMessageRepository.save(msg);
    }

    public List<ChatMessage> findByRoomId(String roomId) {
        return chatMessageRepository.findChatMessageByRoomId(roomId);
    }

    public List<ChatMessage> findByRoomIdAndRead(String roomId, String read) {
        return chatMessageRepository.findByRoomIdAndRead(roomId, read);
    }
}
