package com.oxingaxin.veritas.device.repository

import com.oxingaxin.veritas.device.domain.entity.EntryDevice
import org.springframework.data.jpa.repository.JpaRepository

interface EntryDeviceRepository : JpaRepository<EntryDevice, Long> {
    fun existsByName(name: String): Boolean
}