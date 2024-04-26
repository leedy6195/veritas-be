package com.oxingaxin.veritas.auth.service

import com.oxingaxin.veritas.auth.domain.dto.KakaoUserInfo
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KakaoLoginService {
    private val restTemplate = RestTemplate()
    private val kakaoOauth2GrantType = "authorization_code"
    private val kakaoClientId = "c836f69f636acefd8f6f1927225b6a36"
    private val kakaoRedirectUri = "http://localhost:3000/kakaocallback"

    fun getKakaoAccessToken(code: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", kakaoOauth2GrantType)
        params.add("client_id", kakaoClientId)
        params.add("redirect_uri", kakaoRedirectUri)
        params.add("code", code)


        val entity = HttpEntity(params, headers)
        val response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            entity,
            Map::class.java
        )

        return response.body?.get("access_token") as String
    }

    fun getKakaoUserInfo(accessToken: String): KakaoUserInfo {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val entity = HttpEntity<String>(headers)
        val response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            entity,
            Map::class.java
        )

        val userInfo = response.body?.get("kakao_account") as Map<*, *>
        val email = userInfo["email"] as String
        val profileInfo = userInfo["profile"] as Map<*, *>
        val nickname = profileInfo["nickname"] as String

        return KakaoUserInfo(email, nickname)
    }
}