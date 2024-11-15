package com.yesterpay.puzzle.controller;

import com.yesterpay.puzzle.dto.PuzzleBoard;
import com.yesterpay.puzzle.dto.PuzzleHint;
import com.yesterpay.puzzle.service.PuzzleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}