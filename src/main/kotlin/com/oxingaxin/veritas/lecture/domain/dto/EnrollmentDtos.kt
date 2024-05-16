package com.oxingaxin.veritas.lecture.domain.dto

import com.oxingaxin.veritas.lecture.domain.entity.PaymentMethod

data class EnrollmentRequest(
    val lectureId: Long,
    val studentId: Long,
    val paymentAmount: Long,
    val paymentMethod: PaymentMethod
)

