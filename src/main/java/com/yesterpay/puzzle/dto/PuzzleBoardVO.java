package com.yesterpay.puzzle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleBoardVO {
    private Long wordId;
    private Long crosswordId;
    private Long puzzleTeamId;
    private int startX;
    private int startY;
    private String word;
    private String submitWord;
    private int no;
    private String orientation;
    private String clue;
    private boolean isChecked;
}
