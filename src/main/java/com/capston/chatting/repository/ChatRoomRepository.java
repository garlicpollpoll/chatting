package com.capston.chatting.repository;

import com.capston.chatting.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(attributePaths = "item")
    @Query("select r from ChatRoom r where r.member.id = :memberId")
    List<ChatRoom> findMyRoom(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = "member")
    @Query("select r from ChatRoom r where r.roomId = :roomId and r.member.id = :memberId")
    ChatRoom findByRoomId(@Param("roomId") String roomId, @Param("memberId") Long memberId);

    @Query("select r from ChatRoom r where r.member.id = :memberId and r.item.id = :itemId")
    ChatRoom findByMemberAndItem(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
}
