package com.oxingaxin.veritas.device.domain.dto

import com.oxingaxin.veritas.device.domain.entity.AccessType

data class KioskCreateRequest(
    val name: String,
    val readingRoomId: Long,
)
data class KioskUpdateRequest(
    val name: String,
    val readingRoomId: Long
)

data class KioskResponse(
        val id: Long,
    val name: String,
    val readingRoomName: String,
)

data class EntryDeviceCreateRequest(
    val name: String,
    val accessType: AccessType,
    val lectureRoomId: Long? = null,
    val parentKioskId: Long? = null,
)

data class EntryDeviceResponse(
    val id: Long,
    val name: String,
    val accessType: AccessType,
    val parentKioskName: String?,
    val lectureRoomName: String?,
)
