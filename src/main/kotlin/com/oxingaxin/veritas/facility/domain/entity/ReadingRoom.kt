package com.oxingaxin.veritas.facility.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.oxingaxin.veritas.device.domain.entity.Kiosk
import com.oxingaxin.veritas.facility.domain.dto.ReadingRoomUpdateRequest
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class ReadingRoom(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(unique = true)
        var name: String,

        var width: Int,

        var height: Int,

        var receiverToken: String,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "readingRoom")
        var seats: List<Seat>? = mutableListOf(),

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "readingRoom")
        @OnDelete(action = OnDeleteAction.SET_NULL)
        @JsonIgnore
        var kiosks: List<Kiosk>? = null,

        private var createdAt: LocalDateTime? = null,

        private var updatedAt: LocalDateTime? = null,
) {

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        seats = emptyList()
        kiosks = emptyList()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun update(readingRoomUpdateRequest: ReadingRoomUpdateRequest) {
        name = readingRoomUpdateRequest.name
        width = readingRoomUpdateRequest.width
        height = readingRoomUpdateRequest.height
        receiverToken = readingRoomUpdateRequest.receiverToken
    }

}