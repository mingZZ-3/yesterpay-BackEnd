package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.BingoCellDTO;
import com.yesterpay.bingo.dto.BingoCheckByMissionRequestDTO;
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

    public List<BingoCellDTO> getBingoBoard(Long memberId) {
        List<BingoCellDTO> bingoBoard = bingoMapper.selectBingoBoard(memberId);
        return bingoBoard;
    }

    public BingoStatusResponseDTO getBingoStatus(Long memberId) {
        BingoStatusResponseDTO bingoStatus = bingoMapper.selectBingoStatus(memberId);
        return bingoStatus;
    }

    @Transactional
    public BingoCellDTO checkBingoByMission(BingoCheckByMissionRequestDTO bingoCheckByMissionRequestDTO) {
        bingoMapper.updateBingoCellByIndex(bingoCheckByMissionRequestDTO);
        BingoCellDTO updatedBingoCell = bingoMapper.selectBingoCell(bingoCheckByMissionRequestDTO.getMemberId(), bingoCheckByMissionRequestDTO.getIndex());
        return updatedBingoCell;
    }
}
