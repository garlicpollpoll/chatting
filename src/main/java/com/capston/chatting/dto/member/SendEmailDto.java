package com.capston.chatting.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SendEmailDto {

    private String email;
}
