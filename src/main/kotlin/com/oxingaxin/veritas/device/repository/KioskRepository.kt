package com.oxingaxin.veritas.device.repository

import com.oxingaxin.veritas.device.domain.entity.Kiosk
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface KioskRepository : JpaRepository<Kiosk, Long> {
    fun existsByName(name: String): Boolean

    fun findByEntryDevicesId(id: Long): Optional<Kiosk>
}