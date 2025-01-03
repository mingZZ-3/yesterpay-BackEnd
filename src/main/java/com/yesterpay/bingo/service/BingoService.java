package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.*;
import com.yesterpay.bingo.mapper.BingoMapper;
import com.yesterpay.member.mapper.MemberMapper;
import com.yesterpay.member.service.MemberService;
import com.yesterpay.notification.dto.NotificationInsertDTO;
import com.yesterpay.notification.mapper.NotificationMapper;
import com.yesterpay.notification.service.NotificationService;
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
    private final NotificationService notificationService;
    private final MemberService memberService;

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

        // 빙고와 빙고판 완성 유무 판단 및 처리
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

            // 빙고와 빙고판 완성 유무 판단 및 처리
            int completedResult = isBingoCompleted(bingoCheckByIndexDTO.getMemberId());
            if(completedResult == 1)
                updatedBingoCell.setIsBingoBoardFinished(true);
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

    // return 값이
    // 1 : 빙고가 완성되어 빙고판 완성,  2 : 빙고만 완성,   3 : 아무것도 X
    public int isBingoCompleted(Long memberId) {
        BingoStatusResponseDTO existingBingoStatus = getBingoStatus(memberId);
        BingoBoardDetailDTO bingoBoardDetail = getBingoBoardDetail(memberId);

        int newBingoCount = calculateBingoCount(bingoBoardDetail.getBingoLetterList());
        if (newBingoCount > existingBingoStatus.getBingoCount()) {                      // 새로운 빙고가 완성된 경우
            // 빙고 성공 알림 전송
            notificationService.sendNotification(memberId, newBingoCount + "빙고 완성 !", 1, null);

            // 빙고판 현황의 빙고 개수를 업데이트
            bingoMapper.updateBingoStatus(memberId, bingoBoardDetail.getBingoBoardId(), newBingoCount);

            if (newBingoCount >= existingBingoStatus.getRequiredBingoCount()) {         // 빙고판 자체를 완성한 경우
                // 빙고판 완성 알림 전송
                notificationService.sendNotification(memberId, "빙고판 완성 !! 20 포인트리가 지급 됐습니다 :-)", 1, null);

                Long nextBingoBoardId = bingoBoardDetail.getBingoBoardId() + 1;

                // 다음 빙고판으로 넘어감(= 빙고판 번호 증가)
                memberMapper.increaseBingoBoardId(memberId);
                // 빙고판 현황 테이블에 다음 빙고판을 insert
                bingoMapper.insertBingoBoardStatus(memberId, nextBingoBoardId);
                // 빙고 글자 현황 테이블에 다음 빙고판의 글자들을 insert
                List<Long> bingoLetterIdList = bingoMapper.selectBingoLetterIdListByBoard(nextBingoBoardId);
                bingoMapper.insertBingoLetterStatus(memberId, bingoLetterIdList);
                memberService.insertPoint(memberId,20);

                // 멤버 테이블의 빙고판id, 빙고판 현황 테이블의 빙고판id, 빙고 글자 현황 테이블의 빙고글자id들의 일치 여부를 체크?

                return 1;
            }
            return 2;
        }
        return 3;
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
        // 대각선 검사 \
        if (bingoBoard.get(0).getIsCheck() && bingoBoard.get(4).getIsCheck() && bingoBoard.get(8).getIsCheck()) {
            bingoCount++;
        }
        // 대각선 검사 /
        if (bingoBoard.get(2).getIsCheck() && bingoBoard.get(4).getIsCheck() && bingoBoard.get(6).getIsCheck()) {
            bingoCount++;
        }

        return bingoCount;
    }
}
