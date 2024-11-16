package com.yesterpay.predict.mapper;

import com.yesterpay.predict.dto.HiddenLetter;
import com.yesterpay.predict.dto.PredictResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PredictMapper {
    List<Character> selectPredictLetterCandidateByDateRange(String startDate, String endDate);
    List<HiddenLetter> selectHiddenLetterByDateRange(String startDate, String endDate);
    int insertPredictResult(PredictResult predictResult);
    List<PredictResult> selectPredictHistoryThisWeek(Long memberId);
    int selectPredictSuccessCountThisWeek(Long memberId);
    List<Long> selectMemberListByDateWithPredictSuccess(String date);
}
