package com.yesterpay.bingo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BingoCheckByLetterListDTO {
    private Long memberId;
    private List<Character> letterList;
}
