package com.oxingaxin.veritas.auth.domain.dto



data class AdminCreateRequest(
    val loginId: String,
    val password: String,
    val name: String,
)

data class AdminCreateResponse(
    val id: Long,
)

data class AdminLoginRequest(
    val loginId: String,
    val password: String
)