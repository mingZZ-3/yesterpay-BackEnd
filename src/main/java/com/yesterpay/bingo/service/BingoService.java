package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.BingoCellDTO;
import com.yesterpay.bingo.dto.BingoCheckByIndexDTO;
import com.yesterpay.bingo.dto.BingoCheckByLetterDTO;
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

    // 빙고 미션 성공 시에, 미션이 있던 위치(index)를 받아서 빙고칸을 체크함
    @Transactional
    public BingoCellDTO checkBingoByIndex(BingoCheckByIndexDTO bingoCheckByIndexDTO) {
        bingoMapper.updateBingoCellByIndex(bingoCheckByIndexDTO);
        BingoCellDTO updatedBingoCell = bingoMapper.selectBingoCell(bingoCheckByIndexDTO.getMemberId(), bingoCheckByIndexDTO.getIndex());
        return updatedBingoCell;
    }

    // 글자를 얻은 경우, 해당 글자와 일치하는 빙고칸을 체크함
    @Transactional
    public void checkBingoByLetter(BingoCheckByLetterDTO bingoCheckByLetterDTO) {
        bingoMapper.updateBingoCellByLetter(bingoCheckByLetterDTO);
    }
}
