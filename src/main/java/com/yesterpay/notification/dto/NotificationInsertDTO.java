package com.yesterpay.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class NotificationInsertDTO {
    private Long memberId;
    private String content;
    private Integer type;   // 1: 일반 알림, 2 : 팀신청, 3: 이벤트, 4: 결제
    private Long teamMemberId;
}
