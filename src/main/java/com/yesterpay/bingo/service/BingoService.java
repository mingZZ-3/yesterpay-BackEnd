package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.*;
import com.yesterpay.bingo.mapper.BingoMapper;
import com.yesterpay.member.mapper.MemberMapper;
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
    private final MemberMapper memberMapper;

    public List<BingoCellDTO> getBingoBoard(Long memberId) {
        List<BingoCellDTO> bingoBoard = bingoMapper.selectBingoBoard(memberId);
        return bingoBoard;
    }

    public BingoStatusResponseDTO getBingoStatus(Long memberId) {
        BingoStatusResponseDTO bingoStatus = bingoMapper.selectBingoStatus(memberId);
        return bingoStatus;
    }

    // 글자를 얻은 경우, 해당 글자와 일치하는 빙고칸을 체크함
    @Transactional
    public void checkBingoByLetter(BingoCheckByLetterDTO bingoCheckByLetterDTO) {
        bingoMapper.updateBingoCellByLetter(bingoCheckByLetterDTO);
    }

    public List<BingoCellDTO> getUncheckedBingoLetter(Long memberId) {
        List<BingoCellDTO> uncheckedBingoLetterList = bingoMapper.selectUncheckedBingoLetter(memberId);
        return uncheckedBingoLetterList;
    }

    public BingoMission getBingoMission(Long memberId) {
        BingoMission bingoMission = bingoMapper.selectBingoMission(memberId);
        return bingoMission;
    }

    // 빙고 미션 성공 시에, 미션이 있던 위치(index)를 받아서 빙고칸을 체크함
    @Transactional
    public BingoCellDTO checkBingoByIndex(BingoCheckByIndexDTO bingoCheckByIndexDTO) {
        // 미션 수행 결과가 어떻든, 미션을 시도했기 때문에 다음 미션으로 넘어감(= 미션 번호 증가)
        memberMapper.increaseBingoMissionId(bingoCheckByIndexDTO.getMemberId());

        BingoCellDTO updatedBingoCell = null;
        if (bingoCheckByIndexDTO.getIsSuccess()) {      // 미션 성공 시에만 update와 select 쿼리를 수행
            bingoMapper.updateBingoCellByIndex(bingoCheckByIndexDTO);
            updatedBingoCell = bingoMapper.selectBingoCell(bingoCheckByIndexDTO.getMemberId(), bingoCheckByIndexDTO.getIndex());
        }

        return updatedBingoCell;
    }
}
