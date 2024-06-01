package com.oxingaxin.veritas.student.domain.dto

import com.oxingaxin.veritas.student.domain.entity.CourseType
import com.oxingaxin.veritas.student.domain.entity.GradeType
import java.time.LocalDateTime

data class StudentCreateRequest(
        val name: String,
        val school: String,
        val email: String,
        val birthDate: String,
        val tel: String,
        val parentTel: String?,
        val courseType: CourseType,
        val gradeType: GradeType
)

data class StudentResponse(
        val id: Long,
        val serial: String,
        val name: String,
        val school: String,
        val email: String,
        val birthDate: String,
        val tel: String,
        val parentTel: String?,
        val courseType: CourseType,
        val gradeType: GradeType,
        val createdAt: LocalDateTime,

        )

data class StudentUpdateRequest(
        val name: String,
        val school: String,
        val email: String,
        val birthDate: String,
        val tel: String,
        val parentTel: String?,
        val courseType: CourseType,
        val gradeType: GradeType
)