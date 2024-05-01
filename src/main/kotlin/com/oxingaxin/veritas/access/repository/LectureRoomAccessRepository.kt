package com.oxingaxin.veritas.access.repository


import com.oxingaxin.veritas.access.domain.dto.AttendanceResponse
import com.oxingaxin.veritas.access.domain.entity.LectureRoomAccess
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface LectureRoomAccessRepository : JpaRepository<LectureRoomAccess, Long> {

    @Query("SELECT lra FROM LectureRoomAccess lra " +
            "WHERE lra.lectureRoom.id = :roomId " +
            "AND lra.student.id = :studentId " +
            "AND FUNCTION('DATE', lra.enterTime) = FUNCTION('DATE', CURRENT_TIMESTAMP) " +
            "AND lra.exitTime IS NULL " +
            "ORDER BY lra.enterTime DESC " +
            "LIMIT 1")
    fun findTodayEnter(
            roomId: Long,
            studentId: Long
    ): Optional<LectureRoomAccess>

    @Query("SELECT new com.oxingaxin.veritas.access.domain.dto.AttendanceResponse(" +
            "CONCAT('L', a.id), s.name, s.serial, '강의실', l.name, a.enterTime, a.exitTime, s.courseType) " +
            "FROM LectureRoomAccess a " +
            "JOIN a.student s " +
            "JOIN a.lectureRoom l " +
            "UNION ALL " +
            "SELECT new com.oxingaxin.veritas.access.domain.dto.AttendanceResponse(" +
            "CONCAT('R', a.id), s.name, s.serial, '독서실', r.name, a.enterTime, a.exitTime, s.courseType) " +
            "FROM ReadingRoomAccess a " +
            "JOIN a.student s " +
            "JOIN a.readingRoom r ")
    fun findAttendanceResponses(): List<AttendanceResponse>
}