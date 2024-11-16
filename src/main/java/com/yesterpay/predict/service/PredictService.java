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
        List<Character> predictLetterCandidate = predictMapper.selectPredictLetterCandidateByDateRange(LocalDate.now().toString(), LocalDate.now().toString());
        List<HiddenLetter> hiddenLetterList = predictMapper.selectHiddenLetterByDateRange(LocalDate.now().toString(), LocalDate.now().toString());
        predictLetterCandidate.add(hiddenLetterList.get(0).getLetter());

        return predictLetterCandidate;
    }

    @Transactional
    public void predict(PredictRequestDTO predictRequestDTO) {
        List<HiddenLetter> hiddenLetterList = predictMapper.selectHiddenLetterByDateRange(LocalDate.now().toString(), LocalDate.now().toString());

        boolean isSuccess = false;
        if(hiddenLetterList.get(0).getLetter().equals(predictRequestDTO.getLetter())) {      // 예측에 성공한 경우
            isSuccess = true;
        }

        PredictResult predictResult = new PredictResult(predictRequestDTO.getMemberId(), LocalDate.now().toString(), hiddenLetterList.get(0).getLetter(), predictRequestDTO.getLetter(), isSuccess);
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

    // 매일 8:00에 어제의 모든 예측들을 확인한 후, 이 함수를 호출해야함.
    public int sendPredictSuccessNotification(Long memberId) {
        String today = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        int insertCount = notificationService.sendNotification(memberId, today + "의 히든 글자 예측 성공 !", 1, null);
        return insertCount;
    }
}
