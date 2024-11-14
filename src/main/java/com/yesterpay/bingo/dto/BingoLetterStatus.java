package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoLetterStatus {
    private Long bingoStatusId;
    private Long memberId;
    private Long bingoLetterId;
    private Boolean isCheck;
}
