package com.oxingaxin.veritas.facility.service

import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.facility.domain.dto.*
import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import com.oxingaxin.veritas.facility.domain.entity.Seat
import com.oxingaxin.veritas.facility.repository.ReadingRoomRepository
import com.oxingaxin.veritas.facility.repository.SeatRepository
import com.oxingaxin.veritas.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReadingRoomService(
        private val readingRoomRepository: ReadingRoomRepository,
        private val seatRepository: SeatRepository,
        private val studentRepository: StudentRepository
) {
    fun saveReadingRoom(readingRoomCreateRequest: ReadingRoomCreateRequest): ReadingRoomCreateResponse {
        val readingRoom = ReadingRoom(
            name = readingRoomCreateRequest.name,
            width = readingRoomCreateRequest.width,
            height = readingRoomCreateRequest.height,
            receiverToken = readingRoomCreateRequest.receiverToken
        )
        val saved = readingRoomRepository.save(readingRoom)
        return ReadingRoomCreateResponse(saved.id!!)
    }

    fun updateReadingRoom(roomId: Long, readingRoomUpdateRequest: ReadingRoomUpdateRequest) {
        val readingRoom = findReadingRoomById(roomId)
        //if seat exceed the new width or height, throw exception
        if (readingRoom.seats!!.any { it.x > readingRoomUpdateRequest.width || it.y > readingRoomUpdateRequest.height }) {
            throw IllegalArgumentException("현재 좌석이 변경하려는 가로, 세로 크기를 초과하고 있습니다. 좌석을 이동한 후 다시 시도해주세요.")
        }
        readingRoom.update(readingRoomUpdateRequest)
    }

    fun deleteReadingRoom(id: Long) {
        val readingRoom = findReadingRoomById(id)
        readingRoomRepository.delete(readingRoom)
    }


    fun findReadingRooms(): List<ReadingRoom> {
        return readingRoomRepository.findAll()
    }


    fun findReadingRoomById(id: Long): ReadingRoom {
        return readingRoomRepository.findById(id).orElseThrow { NotFoundException("readingRoom", "id", id.toString()) }
    }

    fun findSeatById(id: Long): Seat {
        return seatRepository.findById(id).orElseThrow { NotFoundException("seat", "id", id.toString()) }
    }

    fun updateSeat(seatId: Long, seatUpdateRequest: SeatUpdateRequest): SeatUpdateResponse {
        val seat = findSeatById(seatId)
        seat.update(seatUpdateRequest)
        return SeatUpdateResponse(
            id = seat.id!!,
            name = seat.name,
            x = seat.x,
            y = seat.y,
            status = seat.status
        )
    }

    fun saveSeat(roomId: Long, seatCreateRequest: SeatCreateRequest): SeatCreateResponse {
        val readingRoom = findReadingRoomById(roomId)

        val seat = Seat(
            name = seatCreateRequest.name,
            x = seatCreateRequest.x,
            y = seatCreateRequest.y,
            status = seatCreateRequest.status,
            readingRoom = readingRoom
        )
        val saved = seatRepository.save(seat)

        return SeatCreateResponse(
            id = saved.id!!,
            name = saved.name,
            x = saved.x,
            y = saved.y,
            status = saved.status
        )
    }

    fun deleteSeat(id: Long) {
        val seat = findSeatById(id)
        seatRepository.delete(seat)
    }
}