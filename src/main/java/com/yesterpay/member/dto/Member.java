package com.yesterpay.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private long memberId;
    private String id;
    private String pw;
    private Integer point;
    private Integer combiCount;
    private String imgUrl;
    private String nickName;
    private String title;
    private long bingoBoardId;
    private long puzzleTeamId;
    private long bingoMissionId;
}
