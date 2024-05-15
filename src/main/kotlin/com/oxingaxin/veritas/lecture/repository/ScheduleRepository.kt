package com.oxingaxin.veritas.lecture.repository

import com.oxingaxin.veritas.lecture.domain.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long>{
    fun findByLectureId(lectureId: Long): List<Schedule>


}