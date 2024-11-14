package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BingoCellResponseDTO {
    private Long bingoLetterId;
    private Integer index;
    private Character letter;
    private Boolean isCheck;
}
