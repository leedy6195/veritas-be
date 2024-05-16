package com.oxingaxin.veritas.common.scheduler

import com.oxingaxin.veritas.lecture.service.EnrollmentService
import com.oxingaxin.veritas.lecture.service.ScheduleAttendanceService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Component
class AttendanceScheduler(
        private val enrollmentService: EnrollmentService,
        private val scheduleAttendanceService: ScheduleAttendanceService,

        ) {
    @Transactional
    @Scheduled(cron = "0 0,30 * * * *")
    fun runAttendanceScheduler() {
        /**
         * 1. 모든 enrollment 를 불러온다
         * 2. 현재 시간 이전의 모든 enrollment.student, enrollment.lecture.schedules 를 불러온다 Map<Long(studentId), List<Long(scheduleId)>>
         * 3. scheduleAttendance 에 해당 student, schedule 가 있다면 종료
         * 4. 아니라면 LectureRoomAccess 기록과 비교해 AttendanceStatus 를 기록 후 scheduleAttendance 생성
         * */

        val enrollments = enrollmentService.findEnrollments()
        val studentScheduleMap = enrollments.map { enrollment ->
            enrollment.student.id!! to enrollment.lecture.schedules
                .filter { it.date.atTime(it.endTime) <= LocalDateTime.now() }
                .map { it.id!! }.toList()
        }.toMap()

        studentScheduleMap.forEach { (studentId, scheduleIds) ->
            scheduleIds.forEach { scheduleId ->
                scheduleAttendanceService.recordScheduleAttendance(studentId, scheduleId)
            }
        }
    }
}