package com.oxingaxin.veritas.auth.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import lombok.Getter
import java.time.LocalDateTime

@Entity
@Getter
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val loginId: String,

    val name: String,

    val password: String,

    private var createdAt: LocalDateTime? = null
) {

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }
}