package com.yesterpay.member.controller;

import com.yesterpay.member.dto.*;
import com.yesterpay.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public ResponseEntity<Member> memberInfo(@PathVariable("id") Long memberId) {
        Member member = memberService.findOne(memberId);
        if(member == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/member/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Long memberId = memberService.findOneByIdAndPw(loginRequestDTO);
        if(memberId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(memberId);
    }

    @GetMapping("/member/{id}/letter")
    public ResponseEntity<List<Character>> letterCollection(@PathVariable("id") Long memberId) {
        List<Character> letterList = memberService.getLetterList(memberId);
        return ResponseEntity.ok().body(letterList);
    }

    @GetMapping("/member/{id}/payment/is-include-hidden-letter")
    public ResponseEntity<HiddenLetterIncludeResult> isIncludePayment(@PathVariable("id") Long memberId, @RequestParam String date) {
        HiddenLetterIncludeResult hiddenLetterIncludeResult = memberService.getHiddenLetterIncludeResult(memberId, date);
        return ResponseEntity.ok().body(hiddenLetterIncludeResult);
    }

}
