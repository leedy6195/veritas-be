package com.oxingaxin.veritas.lecture.service

import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.lecture.domain.dto.EnrollmentRequest
import com.oxingaxin.veritas.lecture.domain.entity.Enrollment
import com.oxingaxin.veritas.lecture.repository.EnrollmentRepository
import com.oxingaxin.veritas.lecture.repository.LectureRepository
import com.oxingaxin.veritas.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class EnrollmentService(
        private val enrollmentRepository: EnrollmentRepository,
        private val lectureRepository: LectureRepository,
        private val studentRepository: StudentRepository,
) {
    fun enroll(enrollmentRequest: EnrollmentRequest) {
        val lecture = lectureRepository.findById(enrollmentRequest.lectureId)
            .orElseThrow { NotFoundException("강의") }

        val student = studentRepository.findById(enrollmentRequest.studentId)
            .orElseThrow { NotFoundException("학생") }

        enrollmentRepository.save(
            Enrollment(
                lecture = lecture,
                student = student,
                paymentAmount = enrollmentRequest.paymentAmount,
                paymentMethod = enrollmentRequest.paymentMethod
            )
        )


    }

    fun findEnrollments() = enrollmentRepository.findAll()
}