package com.yesterpay.predict.controller;

import com.yesterpay.predict.dto.PredictDTO;
import com.yesterpay.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Boolean> predict(@RequestBody PredictDTO predictDTO) {
        boolean predictResult = predictService.predict(predictDTO);
        return ResponseEntity.ok(predictResult);
    }
}
