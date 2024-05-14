package com.oxingaxin.veritas.lecture.service

import com.oxingaxin.veritas.lecture.domain.dto.LectureCreateRequest
import com.oxingaxin.veritas.lecture.domain.entity.Schedule
import com.oxingaxin.veritas.lecture.repository.LectureRepository
import com.oxingaxin.veritas.lecture.repository.ScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek

@Service
@Transactional
class LectureService(
        private val lectureRepository: LectureRepository,
        private val scheduleRepository: ScheduleRepository
) {

    fun saveLecture(lectureCreateRequest: LectureCreateRequest) {
        val lecture = lectureCreateRequest.toEntity()
        lectureRepository.save(lecture)

        val startDate = lectureCreateRequest.startDate
        val endDate = lectureCreateRequest.endDate

        val schedules = mutableListOf<Schedule>()

        var sequence = 1
        var currentDate = startDate

        while (currentDate <= endDate) {
            val dayOfWeek = currentDate.dayOfWeek

            val (startTime, endTime) = when (dayOfWeek) {
                DayOfWeek.MONDAY -> lectureCreateRequest.monStartTime to lectureCreateRequest.monEndTime
                DayOfWeek.TUESDAY -> lectureCreateRequest.tueStartTime to lectureCreateRequest.tueEndTime
                DayOfWeek.WEDNESDAY -> lectureCreateRequest.wedStartTime to lectureCreateRequest.wedEndTime
                DayOfWeek.THURSDAY -> lectureCreateRequest.thuStartTime to lectureCreateRequest.thuEndTime
                DayOfWeek.FRIDAY -> lectureCreateRequest.friStartTime to lectureCreateRequest.friEndTime
                DayOfWeek.SATURDAY -> lectureCreateRequest.satStartTime to lectureCreateRequest.satEndTime
                DayOfWeek.SUNDAY -> lectureCreateRequest.sunStartTime to lectureCreateRequest.sunEndTime
                null -> null to null
            }

            if (startTime != null && endTime != null) {
                val schedule = Schedule(
                    lecture = lecture,
                    sequence = sequence,
                    date = currentDate,
                    startTime = startTime,
                    endTime = endTime,
                    description = "${dayOfWeek.name} $startTime - $endTime"
                )
                schedules.add(schedule)
                sequence++
            }

            currentDate = currentDate.plusDays(1)
        }

        scheduleRepository.saveAll(schedules)
    }
}