package com.yesterpay.puzzle.mapper;

import com.yesterpay.puzzle.dto.PuzzleBoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PuzzleMapper {
    List<PuzzleBoardVO> getPuzzleBoard(Long teamId);
    List<PuzzleBoardVO> getPuzzleHint(Long teamId);
    PuzzleBoardVO getPuzzleStatus(Long teamId, Long wordId);
}