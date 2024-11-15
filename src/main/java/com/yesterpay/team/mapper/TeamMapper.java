package com.yesterpay.team.mapper;

import com.yesterpay.member.dto.Member;
import com.yesterpay.team.dto.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {
    List<Team> selectAll();
    Team selectById(Long teamId);
    List<Member> selectMembersByTeamId(Long teamId);
    int makeTeam(Team team);
    int addMaster(Long memberId, Long teamId);
    int joinTeam(Long memberId, Long teamId);
    int acceptMember(Long memberId, Long teamId);
    int rejectMember(Long memberId, Long teamId);
    List<Long> getPuzzleWordId(Long teamId);
    int makeStatus(Long teamId, Long wordId);

    Member getMemberById(Long memberId);
    int updateUser(Long teamId, Long memberId);
    int insertAlarm(Long memberId);
}