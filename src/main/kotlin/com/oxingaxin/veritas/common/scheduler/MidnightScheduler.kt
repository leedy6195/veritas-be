package com.oxingaxin.veritas.common.scheduler

import com.oxingaxin.veritas.access.service.AccessService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MidnightScheduler(
        private val accessService: AccessService
) {
    @Scheduled(cron = "0 10 0 * * *")
    fun runAtMidnight() {
        /**
         * 1. 오늘 중 퇴실처리되지 않은 입실을 모두 퇴실처리한다.
         * 2.
         */

    }
}