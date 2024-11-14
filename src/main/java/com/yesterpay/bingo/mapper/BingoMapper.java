package com.yesterpay.bingo.mapper;

import com.yesterpay.bingo.dto.BingoBoardResponseDTO;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BingoMapper {
    List<BingoBoardResponseDTO> selectBingoBoard(Long memberId);
    BingoStatusResponseDTO selectBingoStatus(Long memberId);
}
