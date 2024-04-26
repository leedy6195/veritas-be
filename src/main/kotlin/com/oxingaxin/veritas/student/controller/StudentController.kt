package com.oxingaxin.veritas.student.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.common.Constants
import com.oxingaxin.veritas.common.argumentresolver.LoggedIn
import com.oxingaxin.veritas.student.domain.dto.StudentCreateRequest
import com.oxingaxin.veritas.student.domain.dto.StudentResponse
import com.oxingaxin.veritas.student.domain.dto.StudentUpdateRequest
import com.oxingaxin.veritas.student.service.StudentService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(
        private val studentService: StudentService
) {
    /*
    @PostMapping("/login")
    fun login(
            @RequestBody studentLoginRequest: StudentLoginRequest,
            request: HttpServletRequest
    ): BaseResponse<StudentLoginResponse> {

        val student = studentService.login(studentLoginRequest)

        val session = request.getSession(true)
        session.setAttribute(Constants.LOGIN_STUDENT_ID_SESSION_KEY, student.id)

        return BaseResponse.ok(StudentLoginResponse(student.id!!))
    }
     */

    @GetMapping("/getMyInfo")
    fun getMyInfo(
            @LoggedIn studentId: Long?,
            request: HttpServletRequest
    ): BaseResponse<StudentResponse> {

        if (studentId == null) {
            return BaseResponse.ok(null)
        }
        val info = studentService.getMyInfo(studentId)
        return BaseResponse.ok(info)
    }

    @GetMapping
    fun getStudents(): BaseResponse<List<StudentResponse>> {
        val students = studentService.findStudents()
        return BaseResponse.ok(students)
    }

    @PostMapping
    fun createStudent(
            @RequestBody studentCreateRequest: StudentCreateRequest
    ): BaseResponse<Void> {
        studentService.saveStudent(studentCreateRequest)
        return BaseResponse.ok()
    }


    @PutMapping("/{studentId}")
    fun updateStudent(
            @PathVariable studentId: Long,
            @RequestBody studentUpdateRequest: StudentUpdateRequest
    ): BaseResponse<Void> {
        studentService.updateStudent(studentId, studentUpdateRequest)
        return BaseResponse.ok()
    }

    @DeleteMapping("/{studentId}")
    fun deleteStudent(
            @PathVariable studentId: Long
    ) : BaseResponse<Void> {
        studentService.deleteStudent(studentId)
        return BaseResponse.ok()
    }
}