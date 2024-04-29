package com.oxingaxin.veritas.device.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class EntryDevice(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var name: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "kiosk_id")
        @JsonIgnore
        var kiosk: Kiosk?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lecture_room_id")
        @OnDelete(action = OnDeleteAction.SET_NULL)
        @JsonIgnore
        var lectureRoom: LectureRoom?,

        @Enumerated(EnumType.STRING)
        var accessType: AccessType,

        private var createdAt: LocalDateTime? = null,
) {
        @PrePersist
        fun prePersist() {
                createdAt = LocalDateTime.now()
        }
}

enum class AccessType {
        IN, OUT
}