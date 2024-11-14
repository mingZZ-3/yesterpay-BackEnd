package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BingoLetter {
    private Long bingoLetterId;
    private Long bingoBoardId;
    private Integer index;
    private Character letter;
}
