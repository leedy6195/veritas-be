package com.oxingaxin.veritas.lecture.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity
class Lecture(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String,

        var description: String?,

        var instructor: String,

    var startDate: LocalDate,
    var endDate: LocalDate,

    var monStartTime: LocalTime?,
    var monEndTime: LocalTime?,

    var tueStartTime: LocalTime?,
    var tueEndTime: LocalTime?,

    var wedStartTime: LocalTime?,
    var wedEndTime: LocalTime?,

    var thuStartTime: LocalTime?,
    var thuEndTime: LocalTime?,

    var friStartTime: LocalTime?,
    var friEndTime: LocalTime?,

    var satStartTime: LocalTime?,
    var satEndTime: LocalTime?,

    var sunStartTime: LocalTime?,
    var sunEndTime: LocalTime?,

    var createdAt: LocalDateTime? = null,

) {
    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }




}