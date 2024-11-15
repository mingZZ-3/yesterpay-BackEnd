package com.yesterpay.bingo.controller;

import com.yesterpay.bingo.dto.*;
import com.yesterpay.bingo.service.BingoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BingoController {

    private final BingoService bingoService;

    @GetMapping("/bingo/board")
    public ResponseEntity<BingoBoardDetailDTO> bingoBoard(@RequestParam Long memberId) {
        BingoBoardDetailDTO bingoBoardDetail = bingoService.getBingoBoardDetail(memberId);
        return ResponseEntity.ok(bingoBoardDetail);
    }

    @GetMapping("/bingo/status")
    public ResponseEntity<BingoStatusResponseDTO> bingoStatus(@RequestParam Long memberId) {
        BingoStatusResponseDTO bingoStatus = bingoService.getBingoStatus(memberId);
        return ResponseEntity.ok(bingoStatus);
    }

    @GetMapping("/bingo/letter/rest")
    public ResponseEntity<List<BingoCellDTO>> uncheckedBingoLetter(@RequestParam Long memberId) {
        List<BingoCellDTO> uncheckedBingoLetterList = bingoService.getUncheckedBingoLetter(memberId);
        return ResponseEntity.ok(uncheckedBingoLetterList);
    }

    @GetMapping("/bingo/mission")
    public ResponseEntity<BingoMission> bingoMission(@RequestParam Long memberId) {
        BingoMission bingoMission = bingoService.getBingoMission(memberId);
        return ResponseEntity.ok(bingoMission);
    }

    @PostMapping("/bingo/mission/success")
    public ResponseEntity<BingoCellDTO> missionSuccess(@RequestBody BingoCheckByIndexDTO bingoCheckByIndexDTO) {
        BingoCellDTO updatedBingoCell = bingoService.checkBingoByIndex(bingoCheckByIndexDTO);
        return ResponseEntity.ok(updatedBingoCell);
    }

    // '글자로 빙고 체크' 기능을 테스트 하기 위한 테스트용 api
    @PostMapping("/test/bingo/check/by-letter")
    public ResponseEntity<Integer> bingoCheck(@RequestBody BingoCheckByLetterDTO bingoCheckByIndexDTO) {
        int changedCount = bingoService.checkBingoByLetter(bingoCheckByIndexDTO);
        return ResponseEntity.ok(changedCount);
    }
}
