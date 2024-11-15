package com.yesterpay.puzzle.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleBoard {
    private Long wordId;
    private int x;
    private int y;
    private String letter;
    private boolean isCheck;
    private int no;
    private String orientation;
    private String clue;
}