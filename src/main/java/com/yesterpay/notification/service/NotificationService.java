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
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public Integer getUnreadNotificationCount(Long memberId) {
        Integer unreadNotificationCount = notificationMapper.selectUnreadNotificationCount(memberId);
        return unreadNotificationCount;
    }

    public List<Notification> getNotificationList(Long memberId) {
        List<Notification> notificationList = notificationMapper.selectNotificationList(memberId);
        return notificationList;
    }
}
