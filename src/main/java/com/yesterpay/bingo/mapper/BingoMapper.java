package com.yesterpay.bingo.mapper;

import com.yesterpay.bingo.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BingoMapper {
    List<BingoCellDTO> selectBingoLetterList(Long memberId);
    BingoBoard selectBingoBoard(Long memberId);
    BingoStatusResponseDTO selectBingoStatus(Long memberId);
    int updateBingoCellByIndex(BingoCheckByIndexDTO bingoCheckByIndexDTO);
    int updateBingoCellByLetterList(BingoCheckByLetterListDTO bingoCheckByLetterListDTO);
    BingoCellDTO selectBingoCell(Long memberId, Integer index);
    List<BingoCellDTO> selectUncheckedBingoLetter(Long memberId);
    BingoMission selectBingoMission(Long memberId);
    int updateBingoStatus(Long memberId, Long bingoBoardId, Integer bingoCount);
    int insertBingoBoardStatus(Long memberId, Long bingoBoardId);
    int insertBingoLetterStatus(Long memberId, List<Long> bingoLetterIdList);
    List<Long> selectBingoLetterIdListByBoard(Long bingoBoardId);
}
