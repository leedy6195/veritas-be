package com.oxingaxin.veritas.facility.repository

import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReadingRoomRepository : JpaRepository<ReadingRoom, Long> {
    fun findByKiosksId(id: Long): Optional<ReadingRoom>
}