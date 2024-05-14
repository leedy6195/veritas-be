package com.oxingaxin.veritas.lecture.repository

import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long> {
}