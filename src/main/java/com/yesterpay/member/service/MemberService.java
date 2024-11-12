package com.yesterpay.member.service;

import com.yesterpay.member.dto.Member;
import com.yesterpay.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;

    public Member findOne(Long memberId) {
        Member member = memberMapper.selectOne(memberId);
        return member;
    }

}
