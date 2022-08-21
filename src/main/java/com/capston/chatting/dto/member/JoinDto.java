package com.capston.chatting.dto.member;

import lombok.Data;

@Data
public class JoinDto {

    private String loginId;
    private String loginPw;
    private String name;
}
