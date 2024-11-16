package com.yesterpay.predict.service;

import com.yesterpay.predict.dto.HiddenLetter;
import com.yesterpay.predict.dto.PredictRequestDTO;
import com.yesterpay.predict.dto.PredictResult;
import com.yesterpay.predict.mapper.PredictMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PredictService {

    private final PredictMapper predictMapper;

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
        if(hiddenLetterList.get(0).getLetter().equals(predictRequestDTO.getLetter())) {      // 예측한 글자가 히든 글자가 아닌 경우
            isSuccess = true;
        }

        PredictResult predictResult = new PredictResult(predictRequestDTO.getMemberId(), LocalDate.now().toString(), hiddenLetterList.get(0).getLetter(), predictRequestDTO.getLetter(), isSuccess);
        predictMapper.insertPredictResult(predictResult);
    }

    public List<PredictResult> getPredictHistoryThisWeek(Long memberId) {
        List<PredictResult> predictResultList = predictMapper.selectPredictHistoryThisWeek(memberId);
        return predictResultList;
    }
}
