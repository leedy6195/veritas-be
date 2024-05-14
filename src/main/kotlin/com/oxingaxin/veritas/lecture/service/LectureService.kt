package com.oxingaxin.veritas.lecture.service

import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.lecture.domain.dto.LectureRequest
import com.oxingaxin.veritas.lecture.domain.entity.Lecture
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
    fun findLectures(): List<Lecture> = lectureRepository.findAll()
    fun saveLecture(lectureRequest: LectureRequest) {
        val lecture = lectureRequest.toEntity()
        lectureRepository.save(lecture)

        val startDate = lectureRequest.startDate
        val endDate = lectureRequest.endDate

        val schedules = mutableListOf<Schedule>()

        var sequence = 1
        var currentDate = startDate

        while (currentDate <= endDate) {
            val dayOfWeek = currentDate.dayOfWeek

            val (startTime, endTime) = when (dayOfWeek) {
                DayOfWeek.MONDAY -> lectureRequest.monStartTime to lectureRequest.monEndTime
                DayOfWeek.TUESDAY -> lectureRequest.tueStartTime to lectureRequest.tueEndTime
                DayOfWeek.WEDNESDAY -> lectureRequest.wedStartTime to lectureRequest.wedEndTime
                DayOfWeek.THURSDAY -> lectureRequest.thuStartTime to lectureRequest.thuEndTime
                DayOfWeek.FRIDAY -> lectureRequest.friStartTime to lectureRequest.friEndTime
                DayOfWeek.SATURDAY -> lectureRequest.satStartTime to lectureRequest.satEndTime
                DayOfWeek.SUNDAY -> lectureRequest.sunStartTime to lectureRequest.sunEndTime
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

    fun updateLecture(lectureId: Long, lectureRequest: LectureRequest) {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NotFoundException("강의") }

        with(lectureRequest) {
            lecture.apply {
                name = this@with.name
                description = this@with.description
                instructor = this@with.instructor
                startDate = this@with.startDate
                endDate = this@with.endDate
                monStartTime = this@with.monStartTime
                monEndTime = this@with.monEndTime
                tueStartTime = this@with.tueStartTime
                tueEndTime = this@with.tueEndTime
                wedStartTime = this@with.wedStartTime
                wedEndTime = this@with.wedEndTime
                thuStartTime = this@with.thuStartTime
                thuEndTime = this@with.thuEndTime
                friStartTime = this@with.friStartTime
                friEndTime = this@with.friEndTime
                satStartTime = this@with.satStartTime
                satEndTime = this@with.satEndTime
                sunStartTime = this@with.sunStartTime
                sunEndTime = this@with.sunEndTime
            }
        }
    }
}