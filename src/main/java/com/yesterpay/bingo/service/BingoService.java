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

    public BingoBoardDetailDTO getBingoBoardDetail(Long memberId) {
        BingoBoard bingoBoard = bingoMapper.selectBingoBoard(memberId);
        List<BingoCellDTO> bingoLetterList = bingoMapper.selectBingoLetterList(memberId);
        BingoBoardDetailDTO bingoBoardDetail = new BingoBoardDetailDTO(bingoBoard.getBingoBoardId(), bingoBoard.getLevel(), bingoBoard.getRequiredBingoCount(), bingoLetterList);

        return bingoBoardDetail;
    }

    public BingoStatusResponseDTO getBingoStatus(Long memberId) {
        BingoStatusResponseDTO bingoStatus = bingoMapper.selectBingoStatus(memberId);
        return bingoStatus;
    }

    // 글자를 얻은 경우, 해당 글자와 일치하는 빙고칸을 체크함
    @Transactional
    public int checkBingoByLetterList(BingoCheckByLetterListDTO bingoCheckByLetterListDTO) {
        int changedCount = bingoMapper.updateBingoCellByLetterList(bingoCheckByLetterListDTO);

        // 빙고 완성 유무 판단 및 처리
        isBingoCompleted(bingoCheckByLetterListDTO.getMemberId());

        return changedCount;
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

            // 빙고 완성 유무 판단 및 처리
            isBingoCompleted(bingoCheckByIndexDTO.getMemberId());
        }

        return updatedBingoCell;
    }

    public List<BingoCellDTO> getUncheckedBingoLetter(Long memberId) {
        List<BingoCellDTO> uncheckedBingoLetterList = bingoMapper.selectUncheckedBingoLetter(memberId);
        return uncheckedBingoLetterList;
    }

    public BingoMission getBingoMission(Long memberId) {
        BingoMission bingoMission = bingoMapper.selectBingoMission(memberId);
        return bingoMission;
    }

    public Boolean isBingoCompleted(Long memberId) {
        BingoStatusResponseDTO existingBingoStatus = getBingoStatus(memberId);
        BingoBoardDetailDTO bingoBoardDetail = getBingoBoardDetail(memberId);

        int newBingoCount = calculateBingoCount(bingoBoardDetail.getBingoLetterList());
        if (newBingoCount > existingBingoStatus.getBingoCount()) {                      // 새로운 빙고가 완성된 경우
            // 빙고판 현황의 빙고 개수를 업데이트
            bingoMapper.updateBingoStatus(memberId, bingoBoardDetail.getBingoBoardId(), newBingoCount);

            if (newBingoCount >= existingBingoStatus.getRequiredBingoCount()) {         // 빙고판 자체를 완성한 경우
                Long nextBingoBoardId = bingoBoardDetail.getBingoBoardId() + 1;

                // 다음 빙고판으로 넘어감(= 빙고판 번호 증가)
                memberMapper.increaseBingoBoardId(memberId);
                // 빙고판 현황 테이블에 다음 빙고판을 insert
                bingoMapper.insertBingoBoardStatus(memberId, nextBingoBoardId);
                // 빙고 글자 현황 테이블에 다음 빙고판의 글자들을 insert
                List<Long> bingoLetterIdList = bingoMapper.selectBingoLetterIdListByBoard(nextBingoBoardId);
                bingoMapper.insertBingoLetterStatus(memberId, bingoLetterIdList);

                // 멤버 테이블의 빙고판id, 빙고판 현황 테이블의 빙고판id, 빙고 글자 현황 테이블의 빙고글자id들의 일치 여부를 체크?
            }
            return true;
        }
        return false;
    }

    public int calculateBingoCount(List<BingoCellDTO> bingoBoard) {
        int bingoCount = 0;

        // 행 검사
        for(int i = 0; i < 3; i++) {
            if (bingoBoard.get(i * 3).getIsCheck() && bingoBoard.get(i * 3 + 1).getIsCheck() && bingoBoard.get(i * 3 + 2).getIsCheck()) {
                bingoCount++;
            }
        }
        // 열 검사
        for(int i = 0; i < 3; i++) {
            if (bingoBoard.get(i).getIsCheck() && bingoBoard.get(i + 3).getIsCheck() && bingoBoard.get(i + 6).getIsCheck()) {
                bingoCount++;
            }
        }
        // 대각선 검사1
        if (bingoBoard.get(0).getIsCheck() && bingoBoard.get(4).getIsCheck() && bingoBoard.get(8).getIsCheck()) {
            bingoCount++;
        }
        // 대각선 검사2
        if (bingoBoard.get(2).getIsCheck() && bingoBoard.get(4).getIsCheck() && bingoBoard.get(6).getIsCheck()) {
            bingoCount++;
        }

        return bingoCount;
    }
}
