package com.yesterpay.member.mapper;

import com.yesterpay.member.dto.LoginRequestDTO;
import com.yesterpay.member.dto.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    Member selectOne(Long memberId);
    Long selectOneByIdAndPw(LoginRequestDTO loginRequestDTO);
    List<Character> selectLetterList(Long memberId);
}
