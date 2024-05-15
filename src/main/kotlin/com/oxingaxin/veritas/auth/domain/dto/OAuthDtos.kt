package com.oxingaxin.veritas.auth.domain.dto

data class KakaoUserInfo(
    val phoneNumber: String
)

data class SessionCheckResponse(
        val isLoggedIn: Boolean
)