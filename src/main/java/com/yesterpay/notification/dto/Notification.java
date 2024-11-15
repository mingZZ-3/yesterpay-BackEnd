package com.yesterpay.notification.dto;

import lombok.*;

@Getter @Setter
public class Notification {
    private Long notificationId;
    private Long memberId;
    private String content;
    private Boolean isRead;
    private Integer type;   // 1: 일반 알림, 2 : 팀신청, 3: 이벤트, 4: 결제
    private String createdAt;
}
