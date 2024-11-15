package com.yesterpay.combination.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Combination {
    private Long memberId;
    private List<String> removeLetters;
    private List<String> addLetters;
    private int count;
    private String kakaoHashId;
}
