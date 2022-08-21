package com.capston.chatting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoom room;

    private String roomId;

    @Lob
    private String message;

    private String writeTime;

    private String writer;

    private String nowRead;

    public ChatMessage(ChatRoom room, String roomId, String message, String writeTime, String writer, String nowRead) {
        this.room = room;
        this.roomId = roomId;
        this.message = message;
        this.writeTime = writeTime;
        this.writer = writer;
        this.nowRead = nowRead;
    }
}
