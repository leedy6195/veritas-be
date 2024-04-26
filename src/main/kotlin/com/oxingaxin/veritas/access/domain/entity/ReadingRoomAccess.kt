package com.oxingaxin.veritas.access.domain.entity

import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import com.oxingaxin.veritas.facility.domain.entity.Seat
import com.oxingaxin.veritas.student.domain.entity.Student
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class ReadingRoomAccess(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val student: Student,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reading_room_id")
        val readingRoom: ReadingRoom,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "seat_id")
        val seat: Seat,

        var enterTime: LocalDateTime? = null,

        var exitTime: LocalDateTime? = null,
) {
    @PrePersist
    fun prePersist() {
        enterTime = LocalDateTime.now()
    }
}
