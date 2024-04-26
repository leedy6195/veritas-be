package com.oxingaxin.veritas.auth.repository

import com.oxingaxin.veritas.auth.domain.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByLoginId(loginId: String) : Optional<Admin>

    fun existsByLoginId(loginId: String) : Boolean
}