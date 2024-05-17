package com.oxingaxin.veritas.lecture.repository

import com.oxingaxin.veritas.lecture.domain.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository


interface EnrollmentRepository : JpaRepository<Enrollment, Long> {
    fun countByLectureId(lectureId: Long): Long

    fun findByLectureId(lectureId: Long): List<Enrollment>
}