package com.yesterpay.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Long memberId;
    private String id;
    private String pw;
    private Integer point;
    private Integer combiCount;
    private String imgUrl;
    private String nickName;
    private String title;
    private Long bingoBoardId;
    private Long puzzleTeamId;
    private Long bingoMissionId;
}
