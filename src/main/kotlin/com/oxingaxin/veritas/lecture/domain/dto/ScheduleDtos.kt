package com.oxingaxin.veritas.lecture.domain.dto

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleRequest(
        val date: LocalDate,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val description: String
)
