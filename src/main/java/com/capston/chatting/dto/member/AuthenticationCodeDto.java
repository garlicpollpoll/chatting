package com.capston.chatting.dto.member;

import lombok.Data;

@Data
public class AuthenticationCodeDto {

    private String code;
    private String secretKey;
}
