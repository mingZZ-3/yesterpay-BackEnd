package com.yesterpay.team.dto;

import com.yesterpay.member.dto.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private Long puzzleTeamId;
    private Long crosswordId;
    private Long memberId;
    private List<Member> memberList = new ArrayList<>();

}
