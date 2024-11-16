package com.yesterpay.team.service;

import com.yesterpay.member.dto.Member;
import com.yesterpay.notification.dto.Notification;
import com.yesterpay.team.dto.Team;
import com.yesterpay.team.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamMapper mapper;

    public List<Team> selectAll() {
        List<Team> teamList = mapper.selectAll();

        for (Team team : teamList) {
            team.setMemberList(selectMembersByTeamId(team.getPuzzleTeamId(), team.getMemberId()));
        }
        return teamList;
    }

    public Team selectById(Long teamID) {
        Team team = mapper.selectById(teamID);
        return team;
    }

    public List<Member> selectMembersByTeamId(Long teamID, Long masterId) {
        List<Member> members = mapper.selectMembersByTeamId(teamID);

        for (Member member : members) {
            if (Objects.equals(member.getMemberId(), masterId)) {
                member.setMaster(true);
            } else {
                member.setMaster(false);
            }

            if (member.getLetters() != null) {
                List<String> lettersList = Arrays.asList(member.getLetters().split(","));
                member.setLetterList(lettersList);
            }
        }
        return members;
    }

    public boolean hasPuzzleTeam(Long memberId) {
        Member member = mapper.getMemberById(memberId);
        if (member.getPuzzleTeamId() == 0)
            return false;
        else
            return true;
    }

    public Team makeTeam(Long memberId) {
        if (hasPuzzleTeam(memberId)) {
            return null;
        }

        Team team = new Team();
        team.setMemberId(memberId);
        team.setCrosswordId(1L);

        int result = mapper.makeTeam(team);
        if (result == 0) {
            return null;
        }
        updateUser(team.getPuzzleTeamId(), team.getMemberId());
        mapper.addMaster(memberId, team.getPuzzleTeamId());

        // 십자말 현황 초기 세팅
        List<Long> wordIds = mapper.getPuzzleWordId(team.getPuzzleTeamId());
        for (Long wordId : wordIds) {
            mapper.makeStatus(team.getPuzzleTeamId(), wordId);
        }
        return selectById(team.getPuzzleTeamId());
    }

    public int updateUser(Long teamId, Long memberId) {
        int result = mapper.updateUser(teamId, memberId);
        return result;
    }

    public int joinTeam(Long memberId, Long teamId) {
        int result = mapper.joinTeam(memberId, teamId);
        sendAlarmToMaster(memberId,teamId);
        return result;
    }

    public int acceptMember(Long memberId, Long teamId) {
        int result = mapper.acceptMember(memberId,teamId);
        updateUser(teamId, memberId);
        sendAlarmToMember(memberId);
        return result;
    }

    public int rejectMember(Long memberId, Long teamId) {
        int result = mapper.rejectMember(memberId,teamId);
        return result;
    }

    public int leaveTeam(Long memberId, Long teamId) {
        int result = mapper.rejectMember(memberId,teamId);
        if (result == 0) {
            return 0;
        }
        updateUser(0L, memberId);
        return result;
    }

    public int releaseMember(Long memberId, Long teamId, Long userId) {
        Team team = selectById(teamId);
        if (team.getMemberId() != userId) {
            // 방장이 아님
            return 0;
        }

        int result = mapper.rejectMember(memberId, teamId);
        if (result == 0) {
            return 0;
        }
        updateUser(0L, memberId);
        return result;
    }

    public void sendAlarmToMaster(Long teamMemberId, Long teamId) {
        Member member = mapper.getMemberById(teamMemberId);
        Long masterId = mapper.getTeamById(teamId);

        Notification noti = new Notification();
        noti.setTeamMemberId(teamMemberId);
        noti.setMemberId(masterId);
        noti.setType(2);
        noti.setContent(member.getNickName() + " 님이 팀원 신청을 했습니다.");

        mapper.sendAlarmToMaster(noti);
    }

    public void sendAlarmToMember(Long memberId) {
        Notification noti = new Notification();
        noti.setMemberId(memberId);
        noti.setType(1);
        noti.setContent("팀 가입을 승인 받았습니다 !");

        mapper.sendAlarmToMember(noti);
    }
}
