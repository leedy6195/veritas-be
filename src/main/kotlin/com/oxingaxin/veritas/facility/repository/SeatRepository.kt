package com.oxingaxin.veritas.facility.repository

import com.oxingaxin.veritas.facility.domain.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository: JpaRepository<Seat, Long> {

}