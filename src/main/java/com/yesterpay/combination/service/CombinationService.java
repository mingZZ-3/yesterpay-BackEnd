package com.yesterpay.combination.service;

import com.yesterpay.bingo.dto.BingoCheckByLetterListDTO;
import com.yesterpay.bingo.service.BingoService;
import com.yesterpay.combination.dto.Combination;
import com.yesterpay.combination.mapper.CombinationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CombinationService {
    private final CombinationMapper mapper;
    private final BingoService bingoService;

    @Transactional(readOnly = true)
    public boolean hasSharedId(Combination combination) {
        return mapper.selectSharedId(combination) == 0? false : true;
    }

    @Transactional
    public int addCombiCount(Combination combination) {
        if (hasSharedId(combination)) {
            return -1;
        }

        mapper.addSharedId(combination);
        mapper.addCombiCnt(combination.getMemberId());
        return mapper.getCombiCnt(combination.getMemberId());
    }

    @Transactional
    public List<Character> updateLetters(Long memberId, Combination combination) {
        // 새로운 글자를 전달 받지 못한 경우
        if(combination.getNewLetterList() == null || combination.getNewLetterList().isEmpty())
            return null;

        // 추가할 단어가 기존에 있는지 없는지 확인
        List<Long> newLetters = new ArrayList<>();
        for (Character n : combination.getNewLetterList()) {
            Long hiddenId = mapper.findLetter(n);
            if (hiddenId == null) {
                // DB에 없는 단어인 경우
                mapper.addLetterInDB(n);
                newLetters.add(mapper.findLetter(n));
            } else {
                newLetters.add(hiddenId);
            }
        }

        List<Character> myletters = mapper.selectLetterList(memberId);
        // 단순 획득
        if (combination.getExistingLetterList() == null || combination.getExistingLetterList().isEmpty()) {
            // 보유글자가 6개 이상인데, 버릴 글자를 넘겨받지 못했을 때
            if (myletters.size() >= 6)
                return null;

            // 단순 획득 시에는, 2개 이상의 글자를 한번에 얻을 수 없음.
            if (newLetters.size() > 1)
                return null;

            mapper.addLetterToMember(newLetters.get(0), memberId);
            // todo : 빙고판 체크하기
        }
        // 글자 교환
        else if (combination.getExistingLetterList().size() == 1){
            // 보유 글자가 6보다 작은데, 지울 글자를 받은 경우
            if (myletters.size() < 6)
                return null;

            // 지우려는 글자가 보유한 글자가 아닌 경우
            if (!myletters.contains(combination.getExistingLetterList().get(0)))
                return null;

            // 글자 교환의 경우, 무조건 1개의 글자와 1개의 글자를 교환해야함.
            if (newLetters.size() != 1)
                return null;

           Long delLetId = mapper.findLetter(combination.getExistingLetterList().get(0));
           mapper.deleteLetter(delLetId, memberId);
           mapper.addLetterToMember(newLetters.get(0), memberId);
            // todo : 빙고판 체크하기
        }
        // 글자 조합
        else if (combination.getExistingLetterList().size() == 2) {
            if (mapper.getCombiCnt(memberId) == 0) {
                // 조합권 없음
                return null;
            }

            for (Character n : combination.getExistingLetterList()) {
                // 지우려는 글자가 보유한 글자가 아닌 경우
                if (!myletters.contains(n))
                    return null;

                Long id = mapper.findLetter(n);
                mapper.deleteLetter(id, memberId);
            }

            for (Long id : newLetters) {
                mapper.addLetterToMember(id, memberId);
            }

            mapper.subCombiCnt(memberId);
            // todo : 빙고판 체크하기
        }
        // 삭제할 글자가 2개를 초과할 수 없음.
        else {
            return null;
        }

        // 획득한 글자를 바탕으로 빙고 체크하기
        BingoCheckByLetterListDTO bingoCheckByLetterListDTO = new BingoCheckByLetterListDTO(memberId, combination.getNewLetterList());
        bingoService.checkBingoByLetterList(bingoCheckByLetterListDTO);

        return mapper.selectLetterList(memberId);
    }
}
