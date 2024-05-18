package com.oxingaxin.veritas.lecture.domain.dto

import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import com.oxingaxin.veritas.lecture.domain.entity.LectureStatus
import java.time.LocalDate
import java.time.LocalTime

data class LectureRequest(
        val name: String,
        val description: String?,
        val instructor: String,
        val fee: Long?,
        val status: LectureStatus,
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
                fee = fee,
                status = status,
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


data class LectureResponse(
        val id: Long,
        val name: String,
        val description: String?,
        val instructor: String,
        val fee: Long?,
        val status: LectureStatus,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val enrolledStudents: Long,
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
    constructor(lecture: Lecture, enrolledStudents: Long) : this(
            id = lecture.id!!,
            name = lecture.name,
            description = lecture.description,
            instructor = lecture.instructor,
            fee = lecture.fee,
            status = lecture.status,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            enrolledStudents = enrolledStudents,
            monStartTime = lecture.monStartTime,
            monEndTime = lecture.monEndTime,
            tueStartTime = lecture.tueStartTime,
            tueEndTime = lecture.tueEndTime,
            wedStartTime = lecture.wedStartTime,
            wedEndTime = lecture.wedEndTime,
            thuStartTime = lecture.thuStartTime,
            thuEndTime = lecture.thuEndTime,
            friStartTime = lecture.friStartTime,
            friEndTime = lecture.friEndTime,
            satStartTime = lecture.satStartTime,
            satEndTime = lecture.satEndTime,
            sunStartTime = lecture.sunStartTime,
            sunEndTime = lecture.sunEndTime,
    )
}

