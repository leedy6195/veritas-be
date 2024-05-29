package com.oxingaxin.veritas.student.repository

import com.oxingaxin.veritas.student.domain.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StudentRepository : JpaRepository<Student, Long> {
    fun findBySerial(serial: String): Optional<Student>

    fun findByEmail(email: String): Optional<Student>
    fun findByTel(tel: String): Optional<Student>
}