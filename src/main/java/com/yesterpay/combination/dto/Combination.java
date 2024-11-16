package com.yesterpay.combination.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Combination {
    private Long memberId;
    private List<Character> existingLetterList;
    private List<Character> newLetterList;
    private int count;
    private String kakaoHashId;
}
