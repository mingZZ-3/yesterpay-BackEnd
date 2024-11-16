package com.yesterpay.predict.service;

import com.yesterpay.predict.dto.HiddenLetter;
import com.yesterpay.predict.dto.PredictDTO;
import com.yesterpay.predict.dto.PredictResult;
import com.yesterpay.predict.mapper.PredictMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PredictService {

    private final PredictMapper predictMapper;

    public List<Character> getTodayPredictLetterCandidate() {
        List<Character> predictLetterCandidate = predictMapper.selectTodayPredictLetterCandidate();
        HiddenLetter hiddenLetter = predictMapper.selectTodayHiddenLetter();
        predictLetterCandidate.add(hiddenLetter.getLetter());

        return predictLetterCandidate;
    }

    @Transactional
    public void predict(PredictDTO predictDTO) {
        HiddenLetter hiddenLetter = predictMapper.selectHiddenLetterByLetter(predictDTO.getLetter());

        boolean isSuccess = true;
        if(hiddenLetter == null) {      // 예측한 글자가 히든 글자가 아닌 경우
            isSuccess = false;
        }

        PredictResult predictResult = new PredictResult(predictDTO.getMemberId(), LocalDate.now().toString(), predictDTO.getLetter(), isSuccess);
        predictMapper.insertPredictResult(predictResult);
    }
}
