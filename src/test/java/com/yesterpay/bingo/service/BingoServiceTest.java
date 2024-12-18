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

import java.util.ArrayList;

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

        //when

        //then
    }
}