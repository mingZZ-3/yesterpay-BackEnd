package com.yesterpay.bingo.mapper;

import com.yesterpay.bingo.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BingoMapper {
    List<BingoCellDTO> selectBingoBoard(Long memberId);
    BingoStatusResponseDTO selectBingoStatus(Long memberId);
    void updateBingoCellByIndex(BingoCheckByIndexDTO bingoCheckByIndexDTO);
    void updateBingoCellByLetter(BingoCheckByLetterDTO bingoCheckByLetterDTO);
    BingoCellDTO selectBingoCell(Long memberId, Integer index);
    List<BingoCellDTO> selectUncheckedBingoLetter(Long memberId);
    BingoMission selectBingoMission(Long missionId);
}
