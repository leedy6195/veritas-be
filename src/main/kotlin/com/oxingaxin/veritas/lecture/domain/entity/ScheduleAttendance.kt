package com.oxingaxin.veritas.lecture.domain.entity

import com.oxingaxin.veritas.student.domain.entity.Student
import jakarta.persistence.*

@Entity
class ScheduleAttendance(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "schedule_id")
        var schedule: Schedule,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id")
        var student: Student,

        var status: AttendanceStatus
) {

}

enum class AttendanceStatus {
    ATTENDED, ABSENT, LATE, EARLY_LEAVE
}