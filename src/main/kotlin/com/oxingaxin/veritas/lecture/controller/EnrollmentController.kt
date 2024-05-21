package com.oxingaxin.veritas.lecture.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.common.argumentresolver.LoggedIn
import com.oxingaxin.veritas.lecture.domain.dto.EnrollmentRequest
import com.oxingaxin.veritas.lecture.domain.entity.Enrollment
import com.oxingaxin.veritas.lecture.service.EnrollmentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {

    @GetMapping
    fun getEnrollments(): BaseResponse<List<Enrollment>> {
        val enrollments = enrollmentService.findEnrollments()
        return BaseResponse.ok(enrollments)
    }
    @PostMapping
    fun enroll(
            @RequestBody enrollmentRequest: EnrollmentRequest
    ): BaseResponse<Unit> {
        enrollmentService.enroll(enrollmentRequest)
        return BaseResponse.ok()
    }

    @PutMapping("/{enrollmentId}")
    fun updateEnrollment(
            @PathVariable enrollmentId: Long,
            @RequestBody enrollmentRequest: EnrollmentRequest
    ) : BaseResponse<Unit> {
        enrollmentService.updateEnrollment(enrollmentId, enrollmentRequest)
        return BaseResponse.ok()
    }

    @DeleteMapping("/{enrollmentId}")
    fun cancelEnrollment(
            @PathVariable enrollmentId: Long
    ) : BaseResponse<Unit> {
        enrollmentService.deleteEnrollment(enrollmentId)
        return BaseResponse.ok()
    }

    @GetMapping("/my")
    fun getMyEnrollments(
            @LoggedIn studentId: Long?
    ): BaseResponse<List<Enrollment>> {

        if (studentId == null) {
            return BaseResponse.ok(null)
        }

        val enrollments = enrollmentService.findEnrollmentsByStudentId(studentId)
        return BaseResponse.ok(enrollments)
    }

}