package com.capston.chatting.entity;

import com.capston.chatting.dto.ChatRoomDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    private String roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ChatRoom(String roomId, Member member, Item item) {
        this.roomId = roomId;
        this.member = member;
        this.item = item;
    }

//    public static ChatRoom create(String name) {
//        ChatRoomDTO room = new ChatRoomDTO();
//
//        room.roomId = UUID.randomUUID().toString();
//        room.name = name;
//
//        return room;
//    }
}
