package com.capston.chatting.repository;

import com.capston.chatting.entity.Member;
import com.capston.chatting.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId = :loginId")
    Member findMemberByLoginId(@Param("loginId") String loginId);

    // Batch 로 휴면회원 확인하는 쿼리
    List<Member> findByUpdateDateBeforeAndStatusEquals(LocalDateTime localDateTime, MemberStatus status);
}
