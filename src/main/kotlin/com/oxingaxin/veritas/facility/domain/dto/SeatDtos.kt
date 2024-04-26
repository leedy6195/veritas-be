package com.oxingaxin.veritas.facility.domain.dto

import com.oxingaxin.veritas.facility.domain.entity.SeatStatus

data class SeatCreateRequest(
    val name: String,
    val x: Int,
    val y: Int,
    val status: SeatStatus
)

data class SeatCreateResponse(
    val id: Long,
    val name: String,
    val x: Int,
    val y: Int,
    val status: SeatStatus
)

data class SeatUpdateRequest(
        val name: String,
        val x: Int,
        val y: Int,
        val status: SeatStatus
)

data class SeatUpdateResponse(
        val id: Long,
        val name: String,
        val x: Int,
        val y: Int,
        val status: SeatStatus
)