package com.oxingaxin.veritas.facility.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class LectureRoom(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(unique = true)
        var name: String,

        private var createdAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }
}