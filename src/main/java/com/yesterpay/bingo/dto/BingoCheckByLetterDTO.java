package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoCheckByLetterDTO {
    private long memberId;
    private char letter;
}
