package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BingoBoard {
    private Long bingoBoardId;
    private Integer level;
    private Integer requiredCount;
}
