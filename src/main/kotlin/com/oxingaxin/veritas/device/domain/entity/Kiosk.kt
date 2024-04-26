package com.oxingaxin.veritas.device.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.oxingaxin.veritas.device.domain.dto.KioskUpdateRequest
import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Kiosk(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(unique = true)
        var name: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reading_room_id")
        @OnDelete(action = OnDeleteAction.SET_NULL)
        @JsonIgnore
        var readingRoom: ReadingRoom,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "kiosk")
        var entryDevices: List<EntryDevice>? = null,

        private var createdAt: LocalDateTime? = null,
) {
    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }

}