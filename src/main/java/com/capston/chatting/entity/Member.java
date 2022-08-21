package com.capston.chatting.entity;

import com.capston.chatting.enums.MemberRole;
import com.capston.chatting.enums.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String loginPw;
    private String name;

    private int score;

    @Enumerated
    private MemberRole role;

    @Enumerated
    private MemberStatus status;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    public Member(String loginId, String loginPw, String name, int score, MemberRole role, MemberStatus status, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.score = score;
        this.role = role;
        this.status = status;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Member setInactive() {
        status = MemberStatus.INACTIVE;
        return this;
    }
}
