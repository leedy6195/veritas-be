package com.oxingaxin.veritas.lecture.service

import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.lecture.domain.dto.LectureRequest
import com.oxingaxin.veritas.lecture.domain.dto.LectureResponse
import com.oxingaxin.veritas.lecture.domain.dto.ScheduleRequest
import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import com.oxingaxin.veritas.lecture.domain.entity.Schedule
import com.oxingaxin.veritas.lecture.repository.EnrollmentRepository
import com.oxingaxin.veritas.lecture.repository.LectureRepository
import com.oxingaxin.veritas.lecture.repository.ScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek

@Service
@Transactional
class LectureService(
        private val lectureRepository: LectureRepository,
        private val scheduleRepository: ScheduleRepository,
        private val enrollmentRepository: EnrollmentRepository
) {

    fun findSchedules(lectureId: Long): List<Schedule> {
        return scheduleRepository.findByLectureId(lectureId)
            .sortedWith(compareBy<Schedule> { it.date }.thenBy { it.startTime })
    }

    fun findLecture(lectureId: Long): Lecture {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NotFoundException("강의") }

        lecture.schedules =
            lecture.schedules.sortedWith(compareBy<Schedule> { it.date }.thenBy { it.startTime }).toMutableList()
        return lecture

    }

    fun findLectures(): List<LectureResponse> {
        return lectureRepository.findAll().map { lecture ->
            LectureResponse(
                lecture = lecture,
                enrolledStudents = enrollmentRepository.countByLectureId(lecture.id!!)
            )
        }
    }

    fun saveLecture(lectureRequest: LectureRequest) {
        val lecture = lectureRequest.toEntity()
        lectureRepository.save(lecture)

        val startDate = lectureRequest.startDate
        val endDate = lectureRequest.endDate

        val schedules = mutableListOf<Schedule>()

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
                    date = currentDate,
                    startTime = startTime,
                    endTime = endTime,
                    description = "${dayOfWeek.name} $startTime - $endTime"
                )
                schedules.add(schedule)
            }

            currentDate = currentDate.plusDays(1)
        }

        scheduleRepository.saveAll(schedules)
    }

    fun deleteLecture(lectureId: Long) {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NotFoundException("강의") }

        lectureRepository.delete(lecture)
    }

    fun updateLecture(lectureId: Long, lectureRequest: LectureRequest) {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NotFoundException("강의") }

        with(lectureRequest) {
            lecture.apply {
                name = this@with.name
                description = this@with.description
                instructor = this@with.instructor
                fee = this@with.fee
                status = this@with.status
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

    fun saveSchedule(lectureId: Long, scheduleRequest: ScheduleRequest) {
        val lecture = lectureRepository.findById(lectureId)
            .orElseThrow { NotFoundException("강의") }

        val schedule = Schedule(
            lecture = lecture,
            date = scheduleRequest.date,
            startTime = scheduleRequest.startTime,
            endTime = scheduleRequest.endTime,
            description = scheduleRequest.description
        )

        scheduleRepository.save(schedule)
    }

    fun updateSchedule(scheduleId: Long, scheduleRequest: ScheduleRequest) {
        val schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow { NotFoundException("강의 스케줄") }

        schedule.date = scheduleRequest.date
        schedule.startTime = scheduleRequest.startTime
        schedule.endTime = scheduleRequest.endTime
        schedule.description = scheduleRequest.description
    }

    fun deleteSchedule(scheduleId: Long) {
        val schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow { NotFoundException("강의 스케줄") }

        scheduleRepository.delete(schedule)
    }
}