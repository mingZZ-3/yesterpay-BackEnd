package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoCheckByMissionRequestDTO {
    private long memberId;
    private int index;
}
