package com.yesterpay.predict.controller;

import com.yesterpay.predict.dto.PredictRequestDTO;
import com.yesterpay.predict.dto.PredictResult;
import com.yesterpay.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PredictController {

    private final PredictService predictService;

    @GetMapping("/predict/candidate")
    public ResponseEntity<List<Character>> predictCandidate() {
        List<Character> todayPredictLetterCandidate = predictService.getTodayPredictLetterCandidate();
        return ResponseEntity.ok(todayPredictLetterCandidate);
    }

    @PostMapping("/predict/choose")
    public ResponseEntity<Void> predict(@RequestBody PredictRequestDTO predictRequestDTO) {
        predictService.predict(predictRequestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/predict/history/this-week")
    public ResponseEntity<List<PredictResult>> predictHistoryThisWeek(@RequestParam Long memberId) {
        List<PredictResult> predictHistoryList = predictService.getPredictHistoryThisWeek(memberId);
        return ResponseEntity.ok(predictHistoryList);
    }

    @GetMapping("/predict/success-count/this-week")
    public ResponseEntity<Integer> predictSuccessCountThisWeek(@RequestParam Long memberId) {
        int successCount = predictService.getPredictSuccessCount(memberId);
        return ResponseEntity.ok(successCount);
    }
}
