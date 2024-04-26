package com.oxingaxin.veritas.access.domain.dto

import com.oxingaxin.veritas.device.domain.entity.AccessType
import com.oxingaxin.veritas.student.domain.entity.CourseType
import java.time.LocalDateTime

data class ReadingRoomAccessCreateRequest(
        val roomId: Long,
        val seatId: Long,
        val serial: String,
)

data class ReadingRoomAccessCreateResponse(
        val studentName: String,
        val enterTime: LocalDateTime
)

data class ReadingRoomAccessCheckRequest(
        val roomId: Long,
        val serial: String,
)

data class ReadingRoomAccessCheckResponse(
        val seatId: Long
)

data class ReadingRoomExitRequest(
        val deviceId: Long,
        val serial: String,
)

data class ReadingRoomExitResponse(
        val studentName: String,
        val exitTime: LocalDateTime
)

data class LectureRoomAccessRequest(
        val accessType: AccessType,
        val deviceId: Long,
        val serial: String
)

data class LectureRoomAccessResponse(
        val studentName: String,
        val time: LocalDateTime
)

data class AttendanceResponse(
        val studentName: String,
        val studentSerial: String,
        val roomType: String,
        val roomName: String,
        val enterTime: LocalDateTime,
        val exitTime: LocalDateTime?,
        val courseType: CourseType
)

