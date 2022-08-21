package com.capston.chatting.repository;

import com.capston.chatting.entity.ChatMessage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select m from ChatMessage m join fetch m.room r join fetch r.member m2 where m.roomId = :roomId")
    List<ChatMessage> findChatMessageByRoomId(@Param("roomId") String roomId);

    @Query("select m from ChatMessage m where m.roomId = :roomId and m.nowRead = :read")
    List<ChatMessage> findByRoomIdAndRead(@Param("roomId") String roomId, @Param("read") String read);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ChatMessage m set m.nowRead = '1' where m.roomId = :roomId")
    int bulkReadMakeOne(@Param("roomId") String roomId);
}
