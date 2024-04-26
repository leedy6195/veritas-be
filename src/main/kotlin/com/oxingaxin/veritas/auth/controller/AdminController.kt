package com.oxingaxin.veritas.auth.controller

import com.oxingaxin.veritas.auth.domain.dto.AdminCreateRequest
import com.oxingaxin.veritas.auth.domain.dto.AdminCreateResponse
import com.oxingaxin.veritas.auth.domain.dto.AdminLoginRequest
import com.oxingaxin.veritas.auth.domain.dto.SessionCheckResponse
import com.oxingaxin.veritas.auth.service.AdminService
import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.common.Constants
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
        private val adminService: AdminService,
) {
    @PostMapping
    fun createAdmin(
            @RequestBody adminCreateRequest: AdminCreateRequest): BaseResponse<AdminCreateResponse> {
        val admin = adminService.saveAdmin(adminCreateRequest)

        val adminCreateResponse = admin.id?.let { AdminCreateResponse(it) }
        return BaseResponse.builder<AdminCreateResponse>()
            .success(true)
            .status(HttpStatus.CREATED.value())
            .message(Constants.CREATED_MESSAGE)
            .data(adminCreateResponse)
            .build()
    }


    @PostMapping("/login")
    fun login(
            @RequestBody adminLoginRequest: AdminLoginRequest,
            request: HttpServletRequest
    ): BaseResponse<String> {
        val authentication = adminService.login(adminLoginRequest)

        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = authentication

        val session = request.getSession(true)
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext)


        return BaseResponse.builder<String>()
            .success(true)
            .status(HttpStatus.OK.value())
            .message(Constants.OK_MESSAGE)
            .data("login success")
            .build()
    }

    @PostMapping("/logout")
    fun logout(
            request: HttpServletRequest
    ): BaseResponse<Void> {
        request.session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        request.session.invalidate();
        return BaseResponse.builder<Void>()
            .success(true)
            .status(HttpStatus.OK.value())
            .message(Constants.OK_MESSAGE)
            .build()
    }

    @GetMapping("/check")
    fun check(request: HttpServletRequest): BaseResponse<SessionCheckResponse> {
        val session = request.getSession(false)
        val loggedIn = session != null
        return BaseResponse.ok(SessionCheckResponse(loggedIn))
    }
}