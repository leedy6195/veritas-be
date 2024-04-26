package com.oxingaxin.veritas.access.repository

import com.oxingaxin.veritas.access.domain.entity.ReadingRoomAccess
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ReadingRoomAccessRepository : JpaRepository<ReadingRoomAccess, Long> {

    @Query("SELECT rra FROM ReadingRoomAccess rra " +
            "WHERE rra.readingRoom.id = :roomId " +
            "AND rra.student.id = :studentId " +
            "AND FUNCTION('DATE', rra.enterTime) = FUNCTION('DATE', CURRENT_TIMESTAMP) " +
            "ORDER BY rra.enterTime DESC " +
            "LIMIT 1")
    fun findTodayAccess(
            roomId: Long,
            studentId: Long
    ): Optional<ReadingRoomAccess>



    @Query("SELECT rra FROM ReadingRoomAccess rra " +
            "WHERE rra.readingRoom.id = :roomId " +
            "AND rra.student.id = :studentId " +
            "AND FUNCTION('DATE', rra.enterTime) = FUNCTION('DATE', CURRENT_TIMESTAMP) " +
            "AND rra.exitTime IS NULL " +
            "ORDER BY rra.enterTime DESC " +
            "LIMIT 1")
    fun findTodayEnter(
            roomId: Long,
            studentId: Long
    ): Optional<ReadingRoomAccess>

}