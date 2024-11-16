package com.yesterpay.predict.mapper;

import com.yesterpay.predict.dto.HiddenLetter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PredictMapper {
    List<Character> selectTodayPredictLetterCandidate();
    HiddenLetter selectTodayHiddenLetter();
    int insertPredictSuccess(Long memberId, Long hiddenLetterId);
    HiddenLetter selectHiddenLetterByLetter(Character letter);
}
