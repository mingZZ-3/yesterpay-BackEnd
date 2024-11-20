package com.yesterpay.puzzle.service;

import com.yesterpay.member.dto.Member;
import com.yesterpay.member.service.MemberService;
import com.yesterpay.notification.dto.Notification;
import com.yesterpay.puzzle.dto.PuzzleBoard;
import com.yesterpay.puzzle.dto.PuzzleBoardVO;
import com.yesterpay.puzzle.dto.PuzzleHint;
import com.yesterpay.puzzle.dto.SuggestPuzzle;
import com.yesterpay.puzzle.mapper.PuzzleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PuzzleService {
    private final PuzzleMapper mapper;
    private final MemberService memberService;

    public List<PuzzleBoard> getPuzzleBoard(long teamId) {
        // 초기 세팅
        List<PuzzleBoard> puzzleBoard = new ArrayList<>();

        List<PuzzleBoardVO> puzzleData = mapper.getPuzzleBoard(teamId);
        for (PuzzleBoardVO wordData : puzzleData) {
            PuzzleBoardVO status = mapper.getPuzzleStatus(teamId,wordData.getWordId());

            PuzzleBoard cell = new PuzzleBoard();
            cell.setAnswer(wordData.getWord());
            cell.setWordId(wordData.getWordId());
            cell.setStart(new int[]{wordData.getStartX(), wordData.getStartY()});
            cell.setNo(wordData.getNo()); // 글자 순번
            cell.setOrientation(wordData.getOrientation());
            cell.setClue(wordData.getClue());

            if (status.isChecked()) {
                cell.setCheck(true);
                cell.setTeamWord(status.getSubmitWord());
            } else {
                cell.setCheck(false);
                cell.setTeamWord("");
            }
            puzzleBoard.add(cell);
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
        List<SuggestPuzzle> result = mapper.getSuggestWords(teamId);
        List<SuggestPuzzle> suggestPuzzles = new ArrayList<>();
        for (SuggestPuzzle data : result) {
            data.setNecessaryList(mapper.getNecessaryChars(data.getProposalWordId()));
            if (data.getNecessaryList().size() == 0)
                continue;

            data.setSubmitList(mapper.getSubmittedChars(data.getProposalWordId()));
            suggestPuzzles.add(data);
        }
        return suggestPuzzles;
    }

    @Transactional
    public int submitChar(SuggestPuzzle suggestPuzzle) {
        // 유저가 해당 글자를 가지고 있는지 확인
        List<PuzzleBoardVO> letters = mapper.getMyLetters(suggestPuzzle.getMemberId());

        long hiddenLetterId = -1L;
        for (PuzzleBoardVO letter : letters) {
            if (letter.getLetter().equals(suggestPuzzle.getWord())) {
                hiddenLetterId = letter.getHiddenLetterId();
            }
        }

        // 보유한 글자가 아님
        if (hiddenLetterId == -1L) {
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
        int result3 = mapper.removeLetterCollection(suggestPuzzle.getMemberId(), hiddenLetterId);

        if (result1 != 1 || result2 != 1 || result3 != 1) {
            return 0;
        }

        // necessary에 더이상 해당 proposal_word_id가 없을때
        // 해당 word_id 십자말 현황에 반영
        necessaryChars = mapper.getNecessaryChars(suggestPuzzle.getProposalWordId());
        if (necessaryChars.isEmpty()) {
            SuggestPuzzle updatedPuzzle = mapper.getSuggestWordById(suggestPuzzle.getProposalWordId());
            mapper.updatePuzzleStatus(updatedPuzzle);
            checkPuzzleCompletion(suggestPuzzle.getPuzzleTeamId());
        }

        return 1;
    }

    public int getCompletionRate(Long teamId) {
        return mapper.getCompletionRate(teamId);
    }

    public void checkPuzzleCompletion(Long teamId) {
        if (getCompletionRate(teamId) != 100)
            return;

        List<PuzzleBoardVO> words = mapper.getWordStatus(teamId);
        List<Long> memberIds = mapper.getTeamMemberId(teamId);

        for (PuzzleBoardVO word : words) {
            if (!word.getWord().equals(word.getSubmitWord())) {
                // 답과 틀린 단어가 있음
                for (Long memberId : memberIds) {
                    Notification noti = new Notification();
                    noti.setType(1);
                    noti.setContent("십자말에 틀린 답이 있어요. 팀원들과 다시 확인해보세요 !");
                    noti.setMemberId(memberId);
                    mapper.sendPuzzleAlarm(noti);
                }
                return;
            }
        }

        // 십자말 완성
        for (Long memberId : memberIds) {
            Notification noti = new Notification();
            noti.setType(1);
            noti.setContent("십자말을 성공적으로 마무리 했습니다! 다음 게임을 위해 팀원들과 준비해보세요 :-)");
            noti.setMemberId(memberId);
            mapper.sendPuzzleAlarm(noti);
            memberService.insertPoint(memberId,50);
        }
        return;
    }
}