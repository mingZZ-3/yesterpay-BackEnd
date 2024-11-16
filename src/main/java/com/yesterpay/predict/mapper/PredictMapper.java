package com.yesterpay.predict.mapper;

import com.yesterpay.predict.dto.HiddenLetter;
import com.yesterpay.predict.dto.PredictResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PredictMapper {
    List<Character> selectTodayPredictLetterCandidate();
    HiddenLetter selectTodayHiddenLetter();
    int insertPredictResult(PredictResult predictResult);

}
