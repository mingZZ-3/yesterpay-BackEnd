package com.yesterpay.puzzle.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleBoard {
    private Long wordId;
    private int[] start;
    private String teamWord;
    private String answer;
    private boolean isCheck;
    private int no;
    private String orientation;
    private String clue;
    private boolean completion;
}