package com.oxingaxin.veritas.lecture.repository

import com.oxingaxin.veritas.lecture.domain.entity.ScheduleAttendance
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleAttendanceRepository : JpaRepository<ScheduleAttendance, Long> {
    fun existsByStudentIdAndScheduleId(studentId: Long, scheduleId: Long): Boolean
    fun findByScheduleLectureId(lectureId: Long): List<ScheduleAttendance>

}