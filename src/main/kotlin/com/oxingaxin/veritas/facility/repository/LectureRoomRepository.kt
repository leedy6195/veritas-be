package com.oxingaxin.veritas.facility.repository

import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRoomRepository : JpaRepository<LectureRoom, Long> {
}