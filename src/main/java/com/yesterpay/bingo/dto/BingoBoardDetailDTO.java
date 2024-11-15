package com.yesterpay.bingo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class BingoBoardDetailDTO {
    private Long bingoBoardId;
    private Integer level;
    private Integer requiredBingoCount;
    private List<BingoCellDTO> bingoLetterList;
}
