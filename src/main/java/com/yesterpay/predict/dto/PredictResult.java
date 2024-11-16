package com.yesterpay.predict.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PredictResult {
    private Long memberId;
    private String date;
    private Character letter;
    private Boolean isSuccess;
}
