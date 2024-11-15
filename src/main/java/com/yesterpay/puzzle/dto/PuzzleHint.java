package com.yesterpay.puzzle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuzzleHint {
    private List<String> rowHints = new ArrayList<>();
    private List<String> columnHints = new ArrayList<>();
}
