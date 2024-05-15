package com.oxingaxin.veritas.lecture.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Schedule(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lecture_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        var lecture: Lecture,

        var date: LocalDate,

        var startTime: LocalTime,

        var endTime: LocalTime,

        var description: String,


        ) {

}