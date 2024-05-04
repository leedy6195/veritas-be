package com.oxingaxin.veritas.facility.repository

import com.oxingaxin.veritas.facility.domain.entity.Seat
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface SeatFluxRepository : ReactiveCrudRepository<Seat, Long> {
}