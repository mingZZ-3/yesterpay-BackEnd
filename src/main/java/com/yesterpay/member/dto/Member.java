package com.yesterpay.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private String letters;
    private List<String> letterList = new ArrayList<>();
    private boolean isMaster;
}
