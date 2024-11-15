package com.yesterpay.bingo.service;

import com.yesterpay.bingo.dto.BingoCheckByIndexDTO;
import com.yesterpay.bingo.dto.BingoCheckByLetterListDTO;
import com.yesterpay.bingo.dto.BingoStatusResponseDTO;
import com.yesterpay.member.dto.Member;
import com.yesterpay.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BingoServiceTest {

    @Autowired
    BingoService bingoService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("글자 획득 시, 빙고 체크 테스트")
    void 글자_획득시_빙고_체크() {
        //given
        BingoCheckByLetterListDTO bingoCheckByLetterListDTO1 = new BingoCheckByLetterListDTO();
        bingoCheckByLetterListDTO1.setMemberId(1L);
        bingoCheckByLetterListDTO1.setLetter('자');

        BingoCheckByIndexDTO bingoCheckByIndexDTO = new BingoCheckByIndexDTO();
        bingoCheckByIndexDTO.setMemberId(1L);
        bingoCheckByIndexDTO.setIndex(7);
        bingoCheckByIndexDTO.setIsSuccess(true);

        BingoCheckByLetterListDTO bingoCheckByLetterListDTO2 = new BingoCheckByLetterListDTO();
        bingoCheckByLetterListDTO2.setMemberId(1L);
        bingoCheckByLetterListDTO2.setLetter('바');

        //when
        bingoService.checkBingoByLetterList(bingoCheckByLetterListDTO1);
        bingoService.checkBingoByIndex(bingoCheckByIndexDTO);
        bingoService.checkBingoByLetterList(bingoCheckByLetterListDTO2);

        //then
        Member member = memberService.findOne(1L);
        assertEquals(member.getBingoBoardId(), 2L);

        BingoStatusResponseDTO bingoStatus = bingoService.getBingoStatus(1L);
        assertEquals(bingoStatus.getRequiredBingoCount(), 2);
        assertEquals(bingoStatus.getBingoCount(), 0);
    }
}