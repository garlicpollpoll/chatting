package com.capston.chatting.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GetRoomIdDto {

    private String roomId;
    private String itemName;
}
