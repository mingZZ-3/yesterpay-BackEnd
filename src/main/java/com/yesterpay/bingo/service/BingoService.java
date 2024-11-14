package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.BingoCellResponseDTO;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
import com.yesterpay.bingo.mapper.BingoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BingoService {

    private final BingoMapper bingoMapper;

    public List<BingoCellResponseDTO> getBingoBoard(Long memberId) {
        List<BingoCellResponseDTO> bingoBoard = bingoMapper.selectBingoBoard(memberId);
        return bingoBoard;
    }

    public BingoStatusResponseDTO getBingoStatus(Long memberId) {
        BingoStatusResponseDTO bingoStatus = bingoMapper.selectBingoStatus(memberId);
        return bingoStatus;
    }
}
