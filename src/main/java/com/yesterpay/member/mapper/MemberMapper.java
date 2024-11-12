package com.yesterpay.member.mapper;

import com.yesterpay.member.dto.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member selectOne(Long memberId);
}
