package com.oxingaxin.veritas.lecture.domain.entity

import com.oxingaxin.veritas.student.domain.entity.Student
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
class Enrollment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "lecture_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val lecture: Lecture,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "student_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val student: Student,

        var paymentAmount: Long?,

        @Enumerated(EnumType.STRING)
        var paymentMethod: PaymentMethod,

        var createdAt: LocalDateTime? = null,

        ) {
    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
    }

}

enum class PaymentMethod {
    CASH, CREDIT_CARD, BANK_TRANSFER
}
