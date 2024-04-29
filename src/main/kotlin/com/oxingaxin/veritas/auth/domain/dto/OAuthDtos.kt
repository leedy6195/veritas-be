package com.oxingaxin.veritas.auth.domain.dto

data class KakaoUserInfo(
        val email: String
)

data class SessionCheckResponse(
        val isLoggedIn: Boolean
)