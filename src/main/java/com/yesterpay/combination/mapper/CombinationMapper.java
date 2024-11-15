package com.yesterpay.combination.mapper;

import com.yesterpay.combination.dto.Combination;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CombinationMapper {
    int selectSharedId(Combination combination);
    int addSharedId(Combination combination);
    int addCombiCnt(Long memberId);
    int getCombiCnt(Long memberId);
}
