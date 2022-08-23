package com.capston.chatting.entity;

import com.capston.chatting.enums.MemberRole;
import com.capston.chatting.enums.MemberStatus;
import com.capston.chatting.enums.OTPApplyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String loginPw;
    private String name;

    private int score;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Setter
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private OTPApplyStatus otpStatus;

    public Member(String loginId, String loginPw, String name, int score, MemberRole role, MemberStatus status, LocalDateTime updateDate, OTPApplyStatus otpStatus) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.score = score;
        this.role = role;
        this.status = status;
        this.updateDate = updateDate;
        this.otpStatus = otpStatus;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Member setInactive() {
        this.status = MemberStatus.INACTIVE;
        return this;
    }

    public Member setActive() {
        this.status = MemberStatus.ACTIVE;
        return this;
    }

    public Member setOtpApply() {
        this.otpStatus = OTPApplyStatus.APPLY;
        return this;
    }

    public Member setOtpCancel() {
        this.otpStatus = OTPApplyStatus.CANCEL;
        return this;
    }
}
