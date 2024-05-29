package com.oxingaxin.veritas.auth.controller

import com.oxingaxin.veritas.auth.domain.dto.SessionCheckResponse
import com.oxingaxin.veritas.auth.service.KakaoLoginService
import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.common.Constants
import com.oxingaxin.veritas.common.argumentresolver.LoggedIn
import com.oxingaxin.veritas.student.service.StudentService
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class OAuthController(
        private val kakaoLoginService: KakaoLoginService,
        private val studentService: StudentService,
        private val httpSession: HttpSession
) {

    @PostMapping("/login")
    fun login(
            @RequestBody request: LoginRequest
    ): BaseResponse<Void> {
        val email = request.email
        val tel = request.tel

        val optionalStudent = studentService.findByEmail(email)
        if (optionalStudent.isPresent) {
            val student = optionalStudent.get()
            if (student.tel == tel) {
                httpSession.setAttribute(Constants.LOGIN_STUDENT_ID_SESSION_KEY, student.id)
                return BaseResponse.ok()
            }
        }

        return BaseResponse.builder<Void>()
            .success(false)
            .status(HttpStatus.NOT_ACCEPTABLE.value())
            .message("이메일 또는 비밀번호가 일치하지 않습니다.")
            .build()
    }



    @PostMapping("/kakao/token")
    fun getKakaoToken(
            @RequestBody request: KakaoTokenRequest
    ): BaseResponse<KakaoTokenResponse> {

        val code = request.code
        //val state = request.state

        val accessToken = kakaoLoginService.getKakaoAccessToken(code)
        val kakaoUserInfo = kakaoLoginService.getKakaoUserInfo(accessToken)

        // get student with same email
        //val optionalStudent = studentService.findByEmail(kakaoUserInfo.email)
        val optionalStudent = studentService.findByTel(kakaoUserInfo.phoneNumber.replace("+82 ", "0"))
        if (optionalStudent.isPresent) {
            val student = optionalStudent.get()
            httpSession.setAttribute(Constants.LOGIN_STUDENT_ID_SESSION_KEY, student.id)
            val response = KakaoTokenResponse(accessToken)
            return BaseResponse.ok(response)
        } else {
            return BaseResponse.builder<KakaoTokenResponse>()
                .success(false)
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message("등록되지 않은 회원입니다. 관리자에게 문의하세요.")
                .data(KakaoTokenResponse(accessToken))
                .build()
        }
    }

    @GetMapping("/check")
    fun check(@LoggedIn studentId: Long?): BaseResponse<SessionCheckResponse> {
        return BaseResponse.ok(SessionCheckResponse(studentId != null))
    }

    @PostMapping("/logout")
    fun logout(): BaseResponse<Void> {
        httpSession.removeAttribute(Constants.LOGIN_STUDENT_ID_SESSION_KEY)
        httpSession.invalidate()

        return BaseResponse.ok()
    }


}

data class LoginRequest(
        val email: String,
        val tel: String
)

data class KakaoTokenRequest(
        val code: String,
        val state: String
)

data class KakaoTokenResponse(
        val token: String,
)