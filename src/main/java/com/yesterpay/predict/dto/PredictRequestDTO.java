package com.yesterpay.predict.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PredictRequestDTO {
    private Long memberId;
    private Character letter;
}
