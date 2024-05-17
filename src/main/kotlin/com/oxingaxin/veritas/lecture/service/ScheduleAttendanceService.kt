package com.oxingaxin.veritas.lecture.service

import com.oxingaxin.veritas.access.service.AccessService
import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.lecture.domain.entity.AttendanceStatus
import com.oxingaxin.veritas.lecture.domain.entity.ScheduleAttendance
import com.oxingaxin.veritas.lecture.repository.ScheduleAttendanceRepository
import com.oxingaxin.veritas.lecture.repository.ScheduleRepository
import com.oxingaxin.veritas.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ScheduleAttendanceService(
        private val scheduleAttendanceRepository: ScheduleAttendanceRepository,
    private val accessService: AccessService,
        private val scheduleRepository: ScheduleRepository,
    private val studentRepository: StudentRepository
) {
    fun scheduleAttendanceExists(scheduleId: Long, studentId: Long) = scheduleAttendanceRepository.existsByStudentIdAndScheduleId(studentId, scheduleId)

    fun findScheduleAttendances(lectureId: Long): List<ScheduleAttendance> {
        return scheduleAttendanceRepository.findByScheduleLectureId(lectureId)
    }

    fun recordScheduleAttendance(studentId: Long, scheduleId: Long) {
        if (scheduleAttendanceExists(studentId, scheduleId)) {
            return
        }

        val student = studentRepository.findById(studentId)
            .orElseThrow { NotFoundException("학생") }

        val schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow { NotFoundException("강의 스케줄")}

        val attendanceStatus: AttendanceStatus

        val date = schedule.date
        val startTime = schedule.startTime
        val endTime = schedule.endTime

        val startDateTime = date.atTime(startTime)
        val endDateTime = date.atTime(endTime)

        accessService.getAttendanceStatus(studentId, startDateTime, endDateTime)
            .also { attendanceStatus = it }

        val scheduleAttendance = ScheduleAttendance(
            student = student,
            schedule = schedule,
            status = attendanceStatus
        )

        scheduleAttendanceRepository.save(scheduleAttendance)

    }
}