package com.yesterpay.notification.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    Integer selectUnreadNotificationCount(Long memberId);
}
