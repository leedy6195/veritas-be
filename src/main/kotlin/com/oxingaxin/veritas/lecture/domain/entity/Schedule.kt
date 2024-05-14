package com.oxingaxin.veritas.lecture.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Schedule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lecture_id")
        var lecture: Lecture,

        var sequence: Int,

        var date: LocalDate,

        var startTime: LocalTime,

        var endTime: LocalTime,

        var description: String,


        ) {

}