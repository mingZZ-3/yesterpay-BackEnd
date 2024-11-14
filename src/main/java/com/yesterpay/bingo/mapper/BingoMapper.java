package com.yesterpay.bingo.mapper;

import com.yesterpay.bingo.dto.BingoCellResponseDTO;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BingoMapper {
    List<BingoCellResponseDTO> selectBingoBoard(Long memberId);
    BingoStatusResponseDTO selectBingoStatus(Long memberId);
}
