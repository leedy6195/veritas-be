package com.oxingaxin.veritas.facility.repository

import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReadingRoomFluxRepository : ReactiveCrudRepository<ReadingRoom, Long> {
}