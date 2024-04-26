package com.oxingaxin.veritas.auth.service

import com.oxingaxin.veritas.auth.repository.AdminRepository
import com.oxingaxin.veritas.common.exception.NotFoundException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AdminDetailsService(
        private val adminRepository: AdminRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val admin = adminRepository.findByLoginId(username)
            .orElseThrow{ NotFoundException("admin", "loginId", username) }

        return User.builder()
            .username(admin.loginId)
            .password(admin.password)
            .roles("ADMIN")
            .build()
    }
}