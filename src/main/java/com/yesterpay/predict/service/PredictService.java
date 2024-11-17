package com.yesterpay.predict.service;

import com.yesterpay.notification.service.NotificationService;
import com.yesterpay.predict.dto.HiddenLetter;
import com.yesterpay.predict.dto.PredictRequestDTO;
import com.yesterpay.predict.dto.PredictResult;
import com.yesterpay.predict.mapper.PredictMapper;
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
public class PredictService {

    private final PredictMapper predictMapper;
    private final NotificationService notificationService;

    public List<Character> getTodayPredictLetterCandidate() {
        List<Character> predictLetterCandidate = predictMapper.selectTodayPredictCandidate();
        HiddenLetter hiddenLetter = predictMapper.selectTodayHiddenLetter();
        predictLetterCandidate.add(hiddenLetter.getLetter());

        return predictLetterCandidate;
    }

    @Transactional
    public void predict(PredictRequestDTO predictRequestDTO) {
        HiddenLetter hiddenLetter = predictMapper.selectTodayHiddenLetter();

        boolean isSuccess = false;
        if(hiddenLetter.getLetter().equals(predictRequestDTO.getLetter())) {      // 예측에 성공한 경우
            isSuccess = true;
        }

        PredictResult predictResult = new PredictResult(predictRequestDTO.getMemberId(), LocalDate.now().toString(), hiddenLetter.getLetter(), predictRequestDTO.getLetter(), isSuccess);
        predictMapper.insertPredictResult(predictResult);
    }

    public List<PredictResult> getPredictHistoryThisWeek(Long memberId) {
        List<PredictResult> predictResultList = predictMapper.selectPredictHistoryThisWeek(memberId);
        return predictResultList;
    }

    public int getPredictSuccessCount(Long memberId) {
        int successCount = predictMapper.selectPredictSuccessCountThisWeek(memberId);
        return successCount;
    }

    // 매일 8:00에 이 함수를 호출해야함.
    @Transactional
    public int checkYesterdayPredictSuccess() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String message = yesterday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "에 히든 글자 예측 성공! 글자를 획득하세요!";

        List<Long> memberList = predictMapper.selectMemberListByDateWithPredictSuccess(yesterday.toString());
        for (Long memberId : memberList) {
            notificationService.sendNotification(memberId, message, 1, null);
        }

        return memberList.size();
    }
}
