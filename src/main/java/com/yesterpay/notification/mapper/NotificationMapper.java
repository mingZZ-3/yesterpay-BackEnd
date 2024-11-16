package com.yesterpay.notification.mapper;

import com.yesterpay.notification.dto.Notification;
import com.yesterpay.notification.dto.NotificationInsertDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationMapper {
    Integer selectUnreadNotificationCount(Long memberId);
    List<Notification> selectNotificationList(Long memberId);
    int updateNotification(Long notificationId);
    int insertNotification(NotificationInsertDTO notificationInsertDTO);
}
