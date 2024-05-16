package com.oxingaxin.veritas.lecture.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.lecture.domain.dto.EnrollmentRequest
import com.oxingaxin.veritas.lecture.service.EnrollmentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {
    @PostMapping
    fun enroll(
            @RequestBody enrollmentRequest: EnrollmentRequest
    ): BaseResponse<Unit> {
        enrollmentService.enroll(enrollmentRequest)
        return BaseResponse.ok()
    }

}