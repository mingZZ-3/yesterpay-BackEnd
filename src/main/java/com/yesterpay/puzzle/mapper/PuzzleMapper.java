package com.yesterpay.puzzle.mapper;

import com.yesterpay.puzzle.dto.PuzzleBoardVO;
import com.yesterpay.puzzle.dto.SuggestPuzzle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PuzzleMapper {
    List<PuzzleBoardVO> getPuzzleBoard(Long teamId);
    List<PuzzleBoardVO> getPuzzleHint(Long teamId);
    PuzzleBoardVO getPuzzleStatus(Long teamId, Long wordId);
    int suggestWord(SuggestPuzzle suggestPuzzle);
    int setNecessaryChar(PuzzleBoardVO puzzleBoardVO);

    List<SuggestPuzzle> getSuggestWords(Long teamId);
    SuggestPuzzle getSuggestWordById(Long proposalWordId);
    List<String> getSubmittedChars(Long proposalId);
    List<String> getNecessaryChars(Long proposalId);

    List<String> getMyLetters(Long memberId);
    int removeNecessaryChar(SuggestPuzzle suggestPuzzle);
    int submitChar(SuggestPuzzle suggestPuzzle);
    int updatePuzzleStatus(SuggestPuzzle suggestPuzzle);
}