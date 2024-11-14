package com.yesterpay.bingo.mapper;

import com.yesterpay.bingo.dto.BingoCellDTO;
import com.yesterpay.bingo.dto.BingoCheckByMissionRequestDTO;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BingoMapper {
    List<BingoCellDTO> selectBingoBoard(Long memberId);
    BingoStatusResponseDTO selectBingoStatus(Long memberId);
    void updateBingoCellByIndex(BingoCheckByMissionRequestDTO bingoCheckByMissionRequestDTO);
    BingoCellDTO selectBingoCell(long memberId, int index);
}
