package com.yesterpay.member.service;

import com.yesterpay.member.dto.HiddenLetterIncludeResult;
import com.yesterpay.member.dto.LoginRequestDTO;
import com.yesterpay.member.dto.Member;
import com.yesterpay.member.mapper.MemberMapper;
import com.yesterpay.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberMapper memberMapper;
    private final NotificationService notificationService;

    public Member findOne(Long memberId) {
        Member member = memberMapper.selectOne(memberId);
        return member;
    }

    public Long findOneByIdAndPw(LoginRequestDTO loginRequestDTO) {
        Long memberId = memberMapper.selectOneByIdAndPw(loginRequestDTO);
        return memberId;
    }

    public List<Character> getLetterList(Long memberId) {
        List<Character> letterList = memberMapper.selectLetterList(memberId);
        return letterList;
    }

    // 매일 9:00에 이 함수를 호출해야함.(히든 글자가 9시에 공개되므로)
    @Transactional
    public int checkYesterdayPayment() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String startDateTime = yesterday.atTime(8, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDateTime = yesterday.atTime(23, 0, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String message = yesterday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "에 히든 글자가 포함된 결제처에서 결제했습니다! 글자를 획득하세요!";

        List<Long> memberList = memberMapper.selectMemberListByDateWithIncludeHiddenLetter(startDateTime, endDateTime);
        for (Long memberId : memberList) {
            notificationService.sendNotification(memberId, message, 1, null);
        }

        return memberList.size();
    }

    public HiddenLetterIncludeResult getHiddenLetterIncludeResult(Long memberId, String date) {
        HiddenLetterIncludeResult hiddenLetterIncludeResult = memberMapper.selectHiddenLetterIncludeResult(memberId, date);
        if (hiddenLetterIncludeResult == null) {
            hiddenLetterIncludeResult = new HiddenLetterIncludeResult();
            hiddenLetterIncludeResult.setLetter(memberMapper.selectCharacter(date));
            hiddenLetterIncludeResult.setIsInclude(false);
        } else {
            hiddenLetterIncludeResult.setIsInclude(true);
        }

        return hiddenLetterIncludeResult;
    }

    public void insertPoint(Long memberId, int point) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setPoint(point);
        memberMapper.insertPoint(member);
    }
}