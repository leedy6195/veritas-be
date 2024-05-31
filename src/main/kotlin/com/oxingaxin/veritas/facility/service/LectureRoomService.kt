package com.oxingaxin.veritas.facility.service

import com.oxingaxin.veritas.facility.domain.dto.LectureRoomCreateRequest
import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import com.oxingaxin.veritas.facility.repository.LectureRoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class LectureRoomService(
    private val lectureRoomRepository: LectureRoomRepository
) {
    fun createLectureRoom(lectureRoomCreateRequest: LectureRoomCreateRequest) {
        val lectureRoom = LectureRoom(name = lectureRoomCreateRequest.name, receiverToken = lectureRoomCreateRequest.receiverToken)
        lectureRoomRepository.save(lectureRoom)
    }

    fun findLectureRooms() : List<LectureRoom> {
        return lectureRoomRepository.findAll()
    }

    fun deleteLectureRoom(id: Long) {
        lectureRoomRepository.deleteById(id)
    }

    fun updateLectureRoom(id: Long, lectureRoomCreateRequest: LectureRoomCreateRequest) {
        val lectureRoom = lectureRoomRepository.findById(id).get()
        lectureRoom.name = lectureRoomCreateRequest.name
        lectureRoom.receiverToken = lectureRoomCreateRequest.receiverToken
        lectureRoomRepository.save(lectureRoom)
    }
}