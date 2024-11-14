package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoBoardResponseDTO {
    private Integer index;
    private Character letter;
    private Boolean isCheck;
}
