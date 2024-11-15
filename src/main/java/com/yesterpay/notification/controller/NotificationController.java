package com.yesterpay.notification.controller;

import com.yesterpay.notification.dto.Notification;
import com.yesterpay.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/member/{id}/notification/unread-count")
    public ResponseEntity<Integer> unreadNotificationCount(@PathVariable("id") Long memberId) {
        Integer unreadNotificationCount = notificationService.getUnreadNotificationCount(memberId);
        return ResponseEntity.ok().body(unreadNotificationCount);
    }

    @GetMapping("/member/{id}/notification")
    public ResponseEntity<List<Notification>> getNotification(@PathVariable("id") Long memberId) {
        List<Notification> notificationList = notificationService.getNotificationList(memberId);
        return ResponseEntity.ok().body(notificationList);
    }
}
