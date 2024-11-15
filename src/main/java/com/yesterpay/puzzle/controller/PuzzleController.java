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
import java.util.Objects;

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

    @PostMapping("/puzzle/suggest")
    @Operation(summary = "제안 단어의 글자 제출하기")
    public ResponseEntity<Map<String, Object>> suggestLetter(@RequestBody SuggestPuzzle suggest) {
        int result = service.submitChar(suggest);
        Map<String, Object> response = new HashMap<>();

        if (result == -1) {
            response.put("success", false);
            response.put("msg", "유저에게 없는 글자입니다.");
            return ResponseEntity.badRequest().body(response);
        } else if (result == -2) {
            response.put("success", false);
            response.put("msg", "제출할 수 없는 단어입니다.");
            return ResponseEntity.badRequest().body(response);
        } else if (result == 0) {
            response.put("success", false);
            response.put("msg", "제출을 실패했습니다. 다시 시도해주세요.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("puzzle/rate/{teamId}")
    @Operation(summary = "십자말 완성률")
    public ResponseEntity<Map<String, Object>> getPuzzle(@PathVariable Long teamId) {
        Map<String, Object> result = new HashMap<>();
        int rate = service.getCompletionRate(teamId);
        result.put("completionRate", rate);
        return ResponseEntity.ok(result);
    }
}