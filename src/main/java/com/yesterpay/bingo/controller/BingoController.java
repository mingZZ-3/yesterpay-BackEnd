package com.yesterpay.bingo.controller;

import com.yesterpay.bingo.dto.BingoCellDTO;
import com.yesterpay.bingo.dto.BingoCheckByIndexDTO;
import com.yesterpay.bingo.dto.BingoMission;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
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
    public ResponseEntity<List<BingoCellDTO>> bingoBoard(@RequestParam Long memberId) {
        List<BingoCellDTO> bingoBoard = bingoService.getBingoBoard(memberId);
        return ResponseEntity.ok(bingoBoard);
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
        BingoCellDTO updatedBingoCell = null;
        if (bingoCheckByIndexDTO.getIsSuccess()) {
            updatedBingoCell = bingoService.checkBingoByIndex(bingoCheckByIndexDTO);
        }
        return ResponseEntity.ok(updatedBingoCell);
    }
}
