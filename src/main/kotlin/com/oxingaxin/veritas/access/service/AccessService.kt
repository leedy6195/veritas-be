package com.oxingaxin.veritas.access.service

import com.oxingaxin.veritas.access.domain.dto.*
import com.oxingaxin.veritas.access.domain.entity.LectureRoomAccess
import com.oxingaxin.veritas.access.domain.entity.ReadingRoomAccess
import com.oxingaxin.veritas.access.repository.LectureRoomAccessRepository
import com.oxingaxin.veritas.access.repository.ReadingRoomAccessRepository
import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.device.domain.entity.AccessType
import com.oxingaxin.veritas.device.repository.EntryDeviceRepository
import com.oxingaxin.veritas.device.repository.KioskRepository
import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import com.oxingaxin.veritas.facility.domain.entity.SeatStatus
import com.oxingaxin.veritas.facility.repository.ReadingRoomRepository
import com.oxingaxin.veritas.facility.repository.SeatRepository
import com.oxingaxin.veritas.student.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AccessService(
        private val studentRepository: StudentRepository,
        private val seatRepository: SeatRepository,
        private val readingRoomRepository: ReadingRoomRepository,
        private val readingRoomAccessRepository: ReadingRoomAccessRepository,
        private val kioskRepository: KioskRepository,
        private val deviceRepository: EntryDeviceRepository,
        private val lectureRoomAccessRepository: LectureRoomAccessRepository,
) {
    fun saveReadingRoomAccess(readingRoomAccessCreateRequest: ReadingRoomAccessCreateRequest)
            : ReadingRoomAccessCreateResponse {
        val student = studentRepository.findBySerial(readingRoomAccessCreateRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }
        val room = readingRoomRepository.findById(readingRoomAccessCreateRequest.roomId)
            .orElseThrow { NotFoundException("독서실정보") }
        val seat = seatRepository.findById(readingRoomAccessCreateRequest.seatId)
            .orElseThrow { NotFoundException("좌석정보") }

        // if the seat is already occupied, throw an exception
        if (seat.status == SeatStatus.OCCUPIED) {
            throw RuntimeException("이미 사용중인 좌석입니다.")
        }

        // if user already has a seat in the room, throw an exception
        val todayEnter = readingRoomAccessRepository.findTodayEnter(room.id!!, student.id!!)
        if (todayEnter.isPresent) {
            val seatName = todayEnter.get().seat.name
            throw RuntimeException("이미 ${seatName}번 좌석을 사용중입니다.\n 좌석지정 없이 QR코드를 찍어주세요.")
        }


        seat.status = SeatStatus.OCCUPIED

        val readingRoomAccess = ReadingRoomAccess(
            student = student,
            readingRoom = room,
            seat = seat
        )
        val entry = readingRoomAccessRepository.save(readingRoomAccess)
        return ReadingRoomAccessCreateResponse(student.name, entry.enterTime!!)
    }

    fun deleteReadingRoomAccess(accessId: Long) {
        val readingRoomAccess = readingRoomAccessRepository.findById(accessId)
            .orElseThrow { NotFoundException("입실정보") }
        val seat = readingRoomAccess.seat
        // if deleting access is today's access and is the only one,
        // change the seat status to idle
        if (readingRoomAccess.enterTime!!.toLocalDate() == LocalDateTime.now().toLocalDate()) {
            val todayEnter = readingRoomAccessRepository.findTodayEnter(readingRoomAccess.readingRoom.id!!, readingRoomAccess.student.id!!)
            if (todayEnter.isEmpty) {
                seat.status = SeatStatus.IDLE
            }
        }
        readingRoomAccessRepository.delete(readingRoomAccess)
    }
    fun deleteLectureRoomAccess(accessId: Long) {
        val lectureRoomAccess = lectureRoomAccessRepository.findById(accessId)
            .orElseThrow { NotFoundException("입실정보") }
        lectureRoomAccessRepository.delete(lectureRoomAccess)
    }

    fun findTodaySeatId(readingRoomAccessCheckRequest: ReadingRoomAccessCheckRequest)
            : ReadingRoomAccessCheckResponse? {
        val student = studentRepository.findBySerial(readingRoomAccessCheckRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }
        val readingRoomAccess =
            readingRoomAccessRepository.findTodayAccess(readingRoomAccessCheckRequest.roomId, student.id!!)

        if (readingRoomAccess.isPresent) {
            if (readingRoomAccess.get().exitTime == null) {
                throw RuntimeException("퇴실처리를 하지 않은 입실이 존재합니다. \n퇴실처리 먼저 해주세요.")
            }
        }

        return if (readingRoomAccess.isPresent) {
            ReadingRoomAccessCheckResponse(readingRoomAccess.get().seat.id!!)
        } else {
            null
        }
    }

    fun exitReadingRoom(readingRoomExitRequest: ReadingRoomExitRequest)
            : ReadingRoomExitResponse {
        val student = studentRepository.findBySerial(readingRoomExitRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }

        val exitDevice = deviceRepository.findById(readingRoomExitRequest.deviceId)
            .orElseThrow { NotFoundException("디바이스정보") }
        val kiosk = kioskRepository.findByEntryDevicesId(exitDevice.id!!)
            .orElseThrow { NotFoundException("키오스크정보") }
        val readingRoom = readingRoomRepository.findByKiosksId(kiosk.id!!)
            .orElseThrow { NotFoundException("독서실정보") }
        val readingRoomAccess = readingRoomAccessRepository.findTodayEnter(readingRoom.id!!, student.id!!)

        if (readingRoomAccess.isPresent) {
            val now = LocalDateTime.now()
            readingRoomAccess.get().exitTime = now
            return ReadingRoomExitResponse(student.name, now)

        } else {
            throw NotFoundException("입실 정보")
        }
    }

    /**
     * lectureRoom
     **/

    fun accessLectureRoom(lectureRoomAccessRequest: LectureRoomAccessRequest)
            : LectureRoomAccessResponse {
        val student = studentRepository.findBySerial(lectureRoomAccessRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }
        val lectureRoom = deviceRepository.findById(lectureRoomAccessRequest.deviceId)
            .orElseThrow { throw NotFoundException("디바이스정보") }.lectureRoom!!

        if (lectureRoomAccessRequest.accessType == AccessType.IN) {
            val lectureRoomAccess = LectureRoomAccess(student = student, lectureRoom = lectureRoom)
            if (lectureRoomAccessRepository.findTodayEnter(lectureRoom.id!!, student.id!!).isPresent) {
                throw RuntimeException("퇴실처리를 하지 않은 입실이 존재합니다. \n퇴실처리 먼저 해주세요.")
            }
            val entry = lectureRoomAccessRepository.save(lectureRoomAccess)
            return LectureRoomAccessResponse(student.name, entry.enterTime!!)
        } else {
            val todayLectureRoomAccess = lectureRoomAccessRepository.findTodayEnter(lectureRoom.id!!, student.id!!)
                .orElseThrow { NotFoundException("입실 정보") }
            val now = LocalDateTime.now()
            todayLectureRoomAccess.exitTime = now
            return LectureRoomAccessResponse(student.name, now)
        }
    }

    fun findAttendances(): List<AttendanceResponse> {
        return lectureRoomAccessRepository.findAttendanceResponses().sortedByDescending { it.enterTime }
    }
}