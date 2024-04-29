package com.oxingaxin.veritas.facility.domain.dto

data class ReadingRoomCreateRequest(
        val name: String,
        val width: Int,
        val height: Int,
        val receiverToken: String,
)

data class ReadingRoomCreateResponse(
        val id: Long,
)

data class ReadingRoomUpdateRequest(
        val name: String,
        val width: Int,
        val height: Int,
        val receiverToken: String,
)