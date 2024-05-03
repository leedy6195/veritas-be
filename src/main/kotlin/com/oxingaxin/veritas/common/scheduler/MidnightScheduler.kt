package com.oxingaxin.veritas.common.scheduler

import com.oxingaxin.veritas.access.service.AccessService
import com.oxingaxin.veritas.facility.domain.entity.SeatStatus
import com.oxingaxin.veritas.facility.service.ReadingRoomService
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MidnightScheduler(
        private val accessService: AccessService,
        private val readingRoomService: ReadingRoomService
) {
    @Transactional
    @Scheduled(cron = "0 3 0 * * *")
    fun runAtMidnight() {
        /**
         * 1. 전날 퇴실처리되지 않은 입실을 모두 퇴실처리한다.
         * 2. 각 독서실의 모든 좌석을 IDLE 상태로 만든다.
         */
        accessService.exitAllReadingRoomEnter()

        readingRoomService.findReadingRooms().forEach {
            it.seats?.forEach { seat ->
                seat.status = SeatStatus.IDLE
            }
        }
    }
}