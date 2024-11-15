package com.yesterpay.puzzle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestPuzzle {
    private Long proposalWordId;
    private Long wordId;
    private Long puzzleTeamId;
    private String word;
    private List<String> submitList;
    private List<String> necessaryList;
}
