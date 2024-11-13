package com.yesterpay.member.controller;

import com.yesterpay.member.dto.*;
import com.yesterpay.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public ResponseEntity<Member> memberInfo(@PathVariable("id") Long memberId) {
        Member member = memberService.findOne(memberId);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/member/login")
    public ResponseEntity<Member> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Member member = memberService.findOneByIdAndPw(loginRequestDTO);
        return ResponseEntity.ok().body(member);
    }
}
