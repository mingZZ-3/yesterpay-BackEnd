package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoCheckByIndexDTO {
    private Long memberId;
    private Integer index;
    private Boolean isSuccess;
}
