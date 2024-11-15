package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoBoard {
    private Long bingoBoardId;
    private Integer level;
    private Integer requiredBingoCount;
}
