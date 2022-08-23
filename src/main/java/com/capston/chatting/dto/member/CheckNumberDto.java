package com.capston.chatting.dto.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CheckNumberDto {

    @NotEmpty
    private String checkNum;
}
