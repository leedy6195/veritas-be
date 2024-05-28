package com.oxingaxin.veritas.student.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
class Student(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var serial: String? = null,

        var name: String,

        var school: String,

        var email: String,

        var birthDate: String,

        var tel: String,

        var parentTel: String,

        @Enumerated(EnumType.STRING)
        var courseType: CourseType,

        @Enumerated(EnumType.STRING)
        var gradeType: GradeType,

        var createdAt: LocalDateTime? = null,
) {

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        serial = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
    }
}

enum class CourseType {
    WEEKDAY, WEEKEND, GENERAL
}

enum class GradeType {
    MIDDLE1, MIDDLE2, MIDDLE3, HIGH1, HIGH2, HIGH3, GRADUATE
}