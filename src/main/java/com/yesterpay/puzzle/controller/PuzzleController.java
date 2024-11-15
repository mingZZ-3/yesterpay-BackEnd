package com.yesterpay.puzzle.controller;

import com.yesterpay.puzzle.dto.PuzzleBoard;
import com.yesterpay.puzzle.dto.PuzzleHint;
import com.yesterpay.puzzle.dto.SuggestPuzzle;
import com.yesterpay.puzzle.service.PuzzleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "십자말 API", description = "십자말 게임에 대한 API")
public class PuzzleController {
    private final PuzzleService service;

    @GetMapping("puzzle/board/{teamId}")
    @Operation(summary = "십자말 판 조회")
    public ResponseEntity<List<PuzzleBoard>> getPuzzleBoard(@PathVariable Long teamId) {
        List<PuzzleBoard> result = service.getPuzzleBoard(teamId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("puzzle/board/{teamId}/hint")
    @Operation(summary = "십자말 힌트 조회")
    public ResponseEntity<PuzzleHint> getPuzzleHint(@PathVariable Long teamId) {
        PuzzleHint hint = service.getPuzzleHint(teamId);
        if (hint == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hint);
    }

    @PostMapping("/puzzle/board/word")
    @Operation(summary = "단어 제안하기")
    public ResponseEntity<Map<String, Boolean>> suggestPuzzleWord(@RequestBody SuggestPuzzle suggest) {
        int result = service.suggestWord(suggest);

        Map<String, Boolean> response = new HashMap<>();
        if (result == 0) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/puzzle/board/{teamId}/suggest")
    @Operation(summary = "제안된 단어 리스트 조회")
    public ResponseEntity<List<SuggestPuzzle>> getSuggestWords(@PathVariable Long teamId) {
        List<SuggestPuzzle> result = service.getSuggestWords(teamId);
        return ResponseEntity.ok(result);
    }
}