package com.oxingaxin.veritas.lecture.domain.dto

import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import java.time.LocalDate
import java.time.LocalTime

data class LectureRequest(
        val name: String,
        val description: String?,
        val instructor: String,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val monStartTime: LocalTime?,
        val monEndTime: LocalTime?,
        val tueStartTime: LocalTime?,
        val tueEndTime: LocalTime?,
        val wedStartTime: LocalTime?,
        val wedEndTime: LocalTime?,
        val thuStartTime: LocalTime?,
        val thuEndTime: LocalTime?,
        val friStartTime: LocalTime?,
        val friEndTime: LocalTime?,
        val satStartTime: LocalTime?,
        val satEndTime: LocalTime?,
        val sunStartTime: LocalTime?,
        val sunEndTime: LocalTime?,
) {
    fun toEntity(): Lecture {
        return Lecture(
            name = name,
            description = description,
            instructor = instructor,
            startDate = startDate,
            endDate = endDate,
            monStartTime = monStartTime,
            monEndTime = monEndTime,
            tueStartTime = tueStartTime,
            tueEndTime = tueEndTime,
            wedStartTime = wedStartTime,
            wedEndTime = wedEndTime,
            thuStartTime = thuStartTime,
            thuEndTime = thuEndTime,
            friStartTime = friStartTime,
            friEndTime = friEndTime,
            satStartTime = satStartTime,
            satEndTime = satEndTime,
            sunStartTime = sunStartTime,
            sunEndTime = sunEndTime,
        )
    }
}
