package com.oxingaxin.veritas.student.service

import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.student.domain.dto.StudentCreateRequest
import com.oxingaxin.veritas.student.domain.dto.StudentResponse
import com.oxingaxin.veritas.student.domain.dto.StudentUpdateRequest
import com.oxingaxin.veritas.student.domain.entity.Student
import com.oxingaxin.veritas.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class StudentService(
        private val studentRepository: StudentRepository
) {


    fun getMyInfo(studentId: Long): StudentResponse {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NotFoundException("student") }

        return StudentResponse(
            id = student.id!!,
            serial = student.serial!!,
            name = student.name,
            school = student.school,
            email = student.email,
            birthDate = student.birthDate,
            tel = student.tel,
            parentTel = student.parentTel,
            courseType = student.courseType,
            gradeType = student.gradeType,
            createdAt = student.createdAt!!)
    }

    fun findByTel(tel: String): Optional<Student> {
        return studentRepository.findByTel(tel)
    }

    fun findStudents(): List<StudentResponse> {
        return studentRepository.findAll().map {
            StudentResponse(
                id = it.id!!,
                serial = it.serial!!,
                name = it.name,
                school = it.school,
                email = it.email,
                birthDate = it.birthDate,
                tel = it.tel,
                parentTel = it.parentTel,
                courseType = it.courseType,
                gradeType = it.gradeType,
                createdAt = it.createdAt!!)
        }
    }

    fun saveStudent(studentCreateRequest: StudentCreateRequest) {
        val student = Student(
            name = studentCreateRequest.name,
            school = studentCreateRequest.school,
            email = studentCreateRequest.email,
            birthDate = studentCreateRequest.birthDate,
            tel = studentCreateRequest.tel,
            parentTel = studentCreateRequest.parentTel,
            courseType = studentCreateRequest.courseType,
            gradeType = studentCreateRequest.gradeType,
        )

        studentRepository.save(student)
    }

    fun updateStudent(studentId: Long, studentUpdateRequest: StudentUpdateRequest) {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NotFoundException("회원정보") }

        student.name = studentUpdateRequest.name
        student.school = studentUpdateRequest.school
        student.email = studentUpdateRequest.email
        student.birthDate = studentUpdateRequest.birthDate
        student.tel = studentUpdateRequest.tel
        student.parentTel = studentUpdateRequest.parentTel
        student.courseType = studentUpdateRequest.courseType
        student.gradeType = studentUpdateRequest.gradeType
    }

    fun deleteStudent(studentId: Long) {
        val student = studentRepository.findById(studentId)
            .orElseThrow { NotFoundException("회원정보") }
        studentRepository.delete(student)
    }
}