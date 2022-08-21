package com.capston.chatting.dto.jwt;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginInfoDto {

    private String name;
    private String token;

    @Builder
    public LoginInfoDto(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
