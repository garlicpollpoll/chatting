package com.capston.chatting.enums;

import lombok.Getter;

@Getter
public enum MemberRole {

    ROLE_ADMIN("관리자"), ROLE_USER("일반 사용자");

    private String description;

    MemberRole(String description) {
        this.description = description;
    }
}
