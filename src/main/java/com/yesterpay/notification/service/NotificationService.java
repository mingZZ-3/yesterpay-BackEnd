package com.yesterpay.notification.service;

import com.yesterpay.notification.dto.Notification;
import com.yesterpay.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;


    @Transactional(readOnly = true)
    public Integer getUnreadNotificationCount(Long memberId) {
        Integer unreadNotificationCount = notificationMapper.selectUnreadNotificationCount(memberId);
        return unreadNotificationCount;
    }

    @Transactional
    public List<Notification> getNotificationList(Long memberId) {
        List<Notification> notificationList = notificationMapper.selectNotificationList(memberId);

        for (Notification notification : notificationList) {
            notificationMapper.updateNotification(notification.getNotificationId());
        }

        return notificationList;
    }
}
