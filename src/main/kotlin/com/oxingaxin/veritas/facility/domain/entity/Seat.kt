package com.oxingaxin.veritas.facility.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.oxingaxin.veritas.facility.domain.dto.SeatUpdateRequest
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Seat(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String,

        var x: Int,

        var y: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reading_room_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JsonIgnore
        val readingRoom: ReadingRoom,

        @Enumerated(EnumType.STRING)
        var status: SeatStatus,

        private var createdAt: LocalDateTime? = null,
) {
    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }

    fun update(seatUpdateRequest: SeatUpdateRequest) {
        name = seatUpdateRequest.name
        x = seatUpdateRequest.x
        y = seatUpdateRequest.y
        status = seatUpdateRequest.status
    }
}

enum class SeatStatus {
    IDLE, OCCUPIED, UNAVAILABLE
}