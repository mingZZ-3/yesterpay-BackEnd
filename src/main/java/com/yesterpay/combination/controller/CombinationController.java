package com.yesterpay.combination.controller;

import com.yesterpay.combination.dto.Combination;
import com.yesterpay.combination.service.CombinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "조합 API", description = "글자 조합에 대한 API")
public class CombinationController {

    private final CombinationService service;

    @PostMapping("/combi/increase")
    @Operation(summary = "조합권 증정하기")
    public ResponseEntity<Map<String, Object>> increase(@RequestBody Combination combination) {
        Map<String, Object> response = new HashMap<>();
        int result = service.addCombiCount(combination);

        if (result == -1) {
            response.put("success", false);
            response.put("msg", "이미 공유한 친구입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("combiCount", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/member/{memberId}/letter/new")
    @Operation(summary = "보유 글자 업데이트하기")
    public ResponseEntity<List<String>> newLetter(@PathVariable("memberId") Long memberId, @RequestBody Combination combination) {
        List<String> result = service.updateLetters(memberId, combination);
        if (result == null) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // todo : 스케줄러로 shared 주기적으로 삭제하기
}
