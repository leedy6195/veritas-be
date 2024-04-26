package com.oxingaxin.veritas.auth.service

import com.oxingaxin.veritas.auth.domain.dto.AdminCreateRequest
import com.oxingaxin.veritas.auth.domain.dto.AdminLoginRequest
import com.oxingaxin.veritas.auth.domain.entity.Admin
import com.oxingaxin.veritas.auth.repository.AdminRepository
import com.oxingaxin.veritas.common.exception.AlreadyExistsException
import com.oxingaxin.veritas.common.exception.LoginFailedException
import com.oxingaxin.veritas.common.exception.NotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService


import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminService(
        private val adminRepository: AdminRepository,
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager

) {
    fun login(adminLoginRequest: AdminLoginRequest): Authentication {
        val admin = findAdminByLoginId(adminLoginRequest.loginId)
        if (!passwordEncoder.matches(adminLoginRequest.password, admin.password)) {
            throw LoginFailedException()
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(adminLoginRequest.loginId, adminLoginRequest.password)
        )
        return authentication

    }

    fun saveAdmin(adminCreateRequest: AdminCreateRequest): Admin {
        val loginId = adminCreateRequest.loginId
        if (existsAdminByLoginId(loginId)) {
            throw AlreadyExistsException("admin", "loginId", loginId)
        }

        val password = passwordEncoder.encode(adminCreateRequest.password)
        val admin = Admin(loginId = loginId, password = password, name = adminCreateRequest.name)
        return adminRepository.save(admin)
    }

    fun existsAdminByLoginId(loginId: String): Boolean = adminRepository.existsByLoginId(loginId)

    fun findAdminByLoginId(loginId: String): Admin =
        adminRepository.findByLoginId(loginId).orElseThrow { NotFoundException("admin", "loginId", loginId) }


}