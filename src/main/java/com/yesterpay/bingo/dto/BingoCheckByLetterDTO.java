package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoCheckByLetterDTO {
    private Long memberId;
    private Character letter;
}
