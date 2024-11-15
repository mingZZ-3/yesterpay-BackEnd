package com.yesterpay.puzzle.service;

import com.yesterpay.puzzle.dto.PuzzleBoard;
import com.yesterpay.puzzle.dto.PuzzleBoardVO;
import com.yesterpay.puzzle.dto.PuzzleHint;
import com.yesterpay.puzzle.dto.SuggestPuzzle;
import com.yesterpay.puzzle.mapper.PuzzleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PuzzleService {
    private final PuzzleMapper mapper;

    public List<PuzzleBoard> getPuzzleBoard(long teamId) {
        // 초기 세팅
        List<PuzzleBoard> puzzleBoard = new ArrayList<>();
        Map<String, PuzzleBoard> boardMap = new HashMap<>();

        List<PuzzleBoardVO> puzzleData = mapper.getPuzzleBoard(teamId);
        for (PuzzleBoardVO wordData : puzzleData) {
            PuzzleBoardVO status = mapper.getPuzzleStatus(teamId,wordData.getWordId());

            String word = wordData.getWord();
            int startX = wordData.getStartX();
            int startY = wordData.getStartY();
            String orientation = wordData.getOrientation();

            for (int i = 0; i < word.length(); i++) {
                int x = startX;
                int y = startY;

                if ("r".equalsIgnoreCase(orientation)) {
                    y += i; // 가로 방향으로 이동
                } else if ("c".equalsIgnoreCase(orientation)) {
                    x += i; // 세로 방향으로 이동
                }

                // 범위를 벗어나지 않도록 체크
                if (x < 0 || x >= 7 || y < 0 || y >= 7) {
                    break;
                }

                String key = x + "," + y;
                // PuzzleBoard 객체 생성 및 추가
                PuzzleBoard cell = new PuzzleBoard();

                cell.setWordId(wordData.getWordId());
                cell.setX(x);
                cell.setY(y);
                cell.setNo(wordData.getNo()); // 글자 순번
                cell.setOrientation(orientation);
                cell.setClue(wordData.getClue());

                if (status.isChecked()) {
                    cell.setCheck(true);
                    cell.setLetter(String.valueOf(status.getSubmitWord().charAt(i)));
                } else if (boardMap.containsKey(key) && boardMap.get(key).isCheck()) {
                    cell.setCheck(true);
                    cell.setLetter(boardMap.get(key).getLetter());
                } else {
                    cell.setCheck(false);
                    cell.setLetter("");
                }

                boardMap.put(key, cell);
            }
        }

        // 빈 칸 초기화
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                String key = x + "," + y;
                if (!boardMap.containsKey(key)) {
                    // 빈 칸을 '-'로 초기화
                    PuzzleBoard emptyCell = new PuzzleBoard();
                    emptyCell.setWordId(0L);
                    emptyCell.setX(x);
                    emptyCell.setY(y);
                    emptyCell.setLetter("-");
                    emptyCell.setCheck(false);
                    emptyCell.setNo(0); // 빈 칸은 순번 0
                    emptyCell.setOrientation(null); // 방향 없음
                    emptyCell.setClue(null); // 힌트 없음

                    puzzleBoard.add(emptyCell);
                } else {
                    puzzleBoard.add(boardMap.get(key));
                }
            }
        }

        return puzzleBoard;
    }

    public PuzzleHint getPuzzleHint(Long teamId) {
        PuzzleHint puzzleHint = new PuzzleHint();
        List<String> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        List<PuzzleBoardVO> puzzleData = mapper.getPuzzleHint(teamId);

        for (PuzzleBoardVO data : puzzleData) {
            if (data.getOrientation().equals("r")) {
                rows.add(data.getClue());
            } else {
                columns.add(data.getClue());
            }
        }
        puzzleHint.setRowHints(rows);
        puzzleHint.setColumnHints(columns);

        return puzzleHint;
    }

    @Transactional
    public int suggestWord(SuggestPuzzle suggestPuzzle) {
        // proposal_word 테이블에 넣기
        int result = mapper.suggestWord(suggestPuzzle);
        if (result == 0) {
            return 0;
        }

        // proposal_word_necessary_char 테이블에 넣기
        String word = suggestPuzzle.getWord();
        for (int i = 0; i < word.length(); i++) {
            PuzzleBoardVO cell = new PuzzleBoardVO();
            cell.setProposalWordId(suggestPuzzle.getProposalWordId());
            cell.setNecessaryChar(suggestPuzzle.getWord().charAt(i));
            mapper.setNecessaryChar(cell);
        }
        return result;
    }

    public List<SuggestPuzzle> getSuggestWords(Long teamId) {
        List<SuggestPuzzle> suggestPuzzles = mapper.getSuggestWords(teamId);
        for (SuggestPuzzle suggestPuzzle : suggestPuzzles) {
            suggestPuzzle.setSubmitList(mapper.getSubmittedChars(suggestPuzzle.getProposalWordId()));
            suggestPuzzle.setNecessaryList(mapper.getNecessaryChars(suggestPuzzle.getProposalWordId()));
        }
        return suggestPuzzles;
    }

    @Transactional
    public int submitChar(SuggestPuzzle suggestPuzzle) {
        // 유저가 해당 글자를 가지고 있는지 확인
        List<String> letters = mapper.getMyLetters(suggestPuzzle.getMemberId());
        if (!letters.contains(suggestPuzzle.getWord())) {
            return -1;
        }

        // necessary에 포함된 글자인지 확인
        List<String> necessaryChars = mapper.getNecessaryChars(suggestPuzzle.getProposalWordId());
        if (!necessaryChars.contains(suggestPuzzle.getWord())) {
            return -2;
        }

        // necessary에서 제거하고, submit으로 이동
        int result1 = mapper.removeNecessaryChar(suggestPuzzle);
        int result2 = mapper.submitChar(suggestPuzzle);

        if (result1 != 1 || result2 != 1) {
            return 0;
        }

        // necessary에 더이상 해당 proposal_word_id가 없을때
        // 해당 word_id 십자말 현황에 반영
        necessaryChars = mapper.getNecessaryChars(suggestPuzzle.getProposalWordId());
        if (necessaryChars.isEmpty()) {
            SuggestPuzzle updatedPuzzle = mapper.getSuggestWordById(suggestPuzzle.getProposalWordId());
            mapper.updatePuzzleStatus(updatedPuzzle);
        }

        return 1;
    }

    public int getCompletionRate(Long teamId) {
        return mapper.getCompletionRate(teamId);
    }
}