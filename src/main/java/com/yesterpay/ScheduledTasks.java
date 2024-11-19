package com.yesterpay;

import com.yesterpay.member.service.MemberService;
import com.yesterpay.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final PredictService predictService;
    private final MemberService memberService;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Seoul")
    public void sendNotificationByYesterdayPredictSuccess() {
//        log.info("aaaaaaaaa" + "test!");
        int sendCount = predictService.checkYesterdayPredictSuccess();
    }

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Seoul")
    public void sendNotificationByYesterdayPayment() {
//        log.info("bbbbbbbbb" + "test!");
        int sendCount = memberService.checkYesterdayPayment();
    }

}
