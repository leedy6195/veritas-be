package com.oxingaxin.veritas.access.service

import com.oxingaxin.veritas.access.domain.dto.*
import com.oxingaxin.veritas.access.domain.entity.LectureRoomAccess
import com.oxingaxin.veritas.access.domain.entity.ReadingRoomAccess
import com.oxingaxin.veritas.access.repository.LectureRoomAccessRepository
import com.oxingaxin.veritas.access.repository.ReadingRoomAccessRepository
import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.common.util.ReceiverUtil
import com.oxingaxin.veritas.common.util.SmsRequest
import com.oxingaxin.veritas.common.util.SmsUtil
import com.oxingaxin.veritas.device.domain.entity.AccessType
import com.oxingaxin.veritas.device.repository.EntryDeviceRepository
import com.oxingaxin.veritas.device.repository.KioskRepository
import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import com.oxingaxin.veritas.facility.domain.entity.SeatStatus
import com.oxingaxin.veritas.facility.repository.ReadingRoomRepository
import com.oxingaxin.veritas.facility.repository.SeatRepository
import com.oxingaxin.veritas.lecture.domain.entity.AttendanceStatus
import com.oxingaxin.veritas.student.repository.StudentRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
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
        private val receiverUtil: ReceiverUtil,
        private val smsUtil: SmsUtil

) {

    companion object {
        private val logger = LoggerFactory.getLogger(AccessService::class.java)
    }

    fun saveReadingRoomAccess(readingRoomAccessCreateRequest: ReadingRoomAccessCreateRequest)
            : ReadingRoomAccessCreateResponse {

        val room = readingRoomRepository.findById(readingRoomAccessCreateRequest.roomId)
            .orElseThrow { NotFoundException("독서실정보") }

        openAndCloseDoor(room)

        val student = studentRepository.findBySerial(readingRoomAccessCreateRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }

        val seat = seatRepository.findById(readingRoomAccessCreateRequest.seatId)
            .orElseThrow { NotFoundException("좌석정보") }


        // if user already has a seat in the room, throw an exception
        val todayAccess = readingRoomAccessRepository.findTodayAccess(room.id!!, student.id!!)
        if (todayAccess.isPresent && todayAccess.get().seat.id != seat.id) {
            val seatName = todayAccess.get().seat.name
            throw RuntimeException("이미 ${seatName}번 좌석을 사용중입니다.\n 좌석지정 없이 QR코드를 찍어주세요.")
        }

        if (seat.status == SeatStatus.OCCUPIED) {
            readingRoomAccessRepository.findByReadingRoomAndSeat(room.id!!, seat.id!!).ifPresent {
                if (it.student.id != student.id) {
                    throw RuntimeException("이미 사용중인 좌석입니다.")
                }
            }
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

        readingRoomAccessRepository.delete(readingRoomAccess)

        // if deleting access is today's access and is the only one,
        // change the seat status to idle
        if (readingRoomAccess.enterTime!!.toLocalDate() == LocalDateTime.now().toLocalDate()) {
            val todayAccess = readingRoomAccessRepository.findTodayAccess(
                readingRoomAccess.readingRoom.id!!,
                readingRoomAccess.student.id!!
            )
            if (todayAccess.isEmpty) {
                seat.status = SeatStatus.IDLE
            }
        }
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
                openAndCloseDoor(readingRoomAccess.get().readingRoom)
                throw RuntimeException("퇴실처리를 하지 않은 입실이 존재합니다. \n퇴실처리 먼저 해주세요.")
            }
        }

         if (readingRoomAccess.isPresent) {
             return ReadingRoomAccessCheckResponse(readingRoomAccess.get().seat.id!!)
        } else {
            /*
            openAndCloseDoor(readingRoomRepository.findById(readingRoomAccessCheckRequest.roomId)
                .orElseThrow { NotFoundException("독서실정보") })
             */
            return null
        }
    }

    fun exitReadingRoom(readingRoomExitRequest: ReadingRoomExitRequest)
            : ReadingRoomExitResponse {

        val exitDevice = deviceRepository.findById(readingRoomExitRequest.deviceId)
            .orElseThrow { NotFoundException("디바이스정보") }
        val kiosk = kioskRepository.findByEntryDevicesId(exitDevice.id!!)
            .orElseThrow { NotFoundException("키오스크정보") }
        val room = readingRoomRepository.findByKiosksId(kiosk.id!!)
            .orElseThrow { NotFoundException("독서실정보") }

        openAndCloseDoor(room)

        val student = studentRepository.findBySerial(readingRoomExitRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }

        val readingRoomAccess = readingRoomAccessRepository.findTodayEnter(room.id!!, student.id!!)

        if (readingRoomAccess.isPresent) {
            val now = LocalDateTime.now()
            readingRoomAccess.get().exitTime = now
            return ReadingRoomExitResponse(student.name, now)

        } else {
            throw NotFoundException("입실 정보")
        }
    }

    fun openAndCloseDoor(room: ReadingRoom) {
        GlobalScope.launch {
            ReceiverUtil.mutexMap[room.receiverToken] = ReceiverUtil.mutexMap.getOrDefault(room.receiverToken, 0) + 1
            if (ReceiverUtil.mutexMap[room.receiverToken] == 1) {
                receiverUtil.openDoor(room)
            }
            delay(10000)
            ReceiverUtil.mutexMap[room.receiverToken] = ReceiverUtil.mutexMap.getOrDefault(room.receiverToken, 0) - 1
            if (ReceiverUtil.mutexMap[room.receiverToken] == 0) {
                receiverUtil.closeDoor(room)
            }
        }
    }

    fun exitAllReadingRoomEnter() {
        readingRoomAccessRepository.findYesterdayEnter().forEach {
            it.exitTime = LocalDateTime.now()
        }

    }

    /**
     * lectureRoom
     **/

    fun getAttendanceStatus(studentId: Long, startDateTime: LocalDateTime, endDateTime: LocalDateTime): AttendanceStatus {
        val status = lectureRoomAccessRepository.getAttendanceStatus(studentId, startDateTime, endDateTime)
        return AttendanceStatus.valueOf(status)
    }

    fun accessLectureRoom(lectureRoomAccessRequest: LectureRoomAccessRequest)
            : LectureRoomAccessResponse {

        val lectureRoom = deviceRepository.findById(lectureRoomAccessRequest.deviceId)
            .orElseThrow { throw NotFoundException("디바이스정보") }.lectureRoom!!

        val student = studentRepository.findBySerial(lectureRoomAccessRequest.serial)
            .orElseThrow { NotFoundException("회원정보") }

        if (!lectureRoom.receiverToken.isNullOrEmpty()) {
            receiverUtil.openLectureRoomDoor(lectureRoom)
            logger.info("${student.name}(${student.serial})님이 ${lectureRoom.name}에 입실하였습니다.")
        }



        if (lectureRoomAccessRequest.accessType == AccessType.IN) {
            val lectureRoomAccess = LectureRoomAccess(student = student, lectureRoom = lectureRoom)
            if (lectureRoomAccessRepository.findTodayEnter(lectureRoom.id!!, student.id!!).isPresent) {
                throw RuntimeException("퇴실처리를 하지 않은 입실이 존재합니다. \n퇴실처리 먼저 해주세요.")
            }

            if (lectureRoomAccessRepository.findTodayAnyEnter(student.id!!).isEmpty && !student.parentTel.isNullOrEmpty()) {
                smsUtil.sendSms(
                    SmsRequest(smsUtil.convertTel(student.parentTel!!), smsUtil.convertMessage(student.name, AccessType.IN)))
            }
            val entry = lectureRoomAccessRepository.save(lectureRoomAccess)


            return LectureRoomAccessResponse(student.name, entry.enterTime!!)
        } else {
            val todayLectureRoomAccess = lectureRoomAccessRepository.findTodayEnter(lectureRoom.id!!, student.id!!)
                .orElseThrow { NotFoundException("입실 정보") }
            val now = LocalDateTime.now()
            todayLectureRoomAccess.exitTime = now

            if (!student.parentTel.isNullOrEmpty()) {
                smsUtil.sendSms(
                    SmsRequest(smsUtil.convertTel(student.parentTel!!), smsUtil.convertMessage(student.name, AccessType.OUT)))
            }

            /*
            if (!lectureRoom.receiverToken.isNullOrEmpty()) {
                receiverUtil.openLectureRoomDoor(lectureRoom)
            }
            */
            return LectureRoomAccessResponse(student.name, now)
        }
    }

    fun findAttendances(): List<AttendanceResponse> {
        return lectureRoomAccessRepository.findAttendanceResponses().sortedByDescending { it.enterTime }
    }

    fun findMyAttendances(studentId: Long): List<AttendanceResponse> {
        return lectureRoomAccessRepository.findMyAttendanceResponses(studentId).sortedByDescending { it.enterTime }
    }
}