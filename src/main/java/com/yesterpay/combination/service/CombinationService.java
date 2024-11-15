package com.yesterpay.combination.service;

import com.yesterpay.combination.dto.Combination;
import com.yesterpay.combination.mapper.CombinationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CombinationService {
    private final CombinationMapper mapper;

    @Transactional(readOnly = true)
    public boolean hasSharedId(Combination combination) {
        return mapper.selectSharedId(combination) == 0? false : true;
    }

    @Transactional
    public int addCombiCount(Combination combination) {
        if (hasSharedId(combination)) {
            return -1;
        }

        mapper.addSharedId(combination);
        mapper.addCombiCnt(combination.getMemberId());
        return mapper.getCombiCnt(combination.getMemberId());
    }
}
