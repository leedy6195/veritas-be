package com.oxingaxin.veritas.device.service

import com.oxingaxin.veritas.common.exception.AlreadyExistsException
import com.oxingaxin.veritas.common.exception.NotFoundException
import com.oxingaxin.veritas.device.domain.dto.*
import com.oxingaxin.veritas.device.domain.entity.EntryDevice
import com.oxingaxin.veritas.device.domain.entity.Kiosk
import com.oxingaxin.veritas.device.repository.EntryDeviceRepository
import com.oxingaxin.veritas.device.repository.KioskRepository
import com.oxingaxin.veritas.facility.repository.LectureRoomRepository
import com.oxingaxin.veritas.facility.repository.ReadingRoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeviceService(
        private val kioskRepository: KioskRepository,
        private val entryDeviceRepository: EntryDeviceRepository,
        private val readingRoomRepository: ReadingRoomRepository,
        private val lectureRoomRepository: LectureRoomRepository
) {

    fun saveKiosk(kioskCreateRequest: KioskCreateRequest) {
        if (kioskRepository.existsByName(kioskCreateRequest.name)) {
            throw AlreadyExistsException("키오스크 관리명")
        }
        val readingRoom = readingRoomRepository.findById(kioskCreateRequest.readingRoomId).orElseThrow {
            NotFoundException("독서실")
        }

        val kiosk = Kiosk(name = kioskCreateRequest.name, readingRoom = readingRoom)
        kioskRepository.save(kiosk)
    }

    fun updateKiosk(kioskId: Long, kioskUpdateRequest: KioskUpdateRequest) {
        val kiosk = kioskRepository.findById(kioskId).orElseThrow {
            NotFoundException("키오스크")
        }
        val readingRoom = readingRoomRepository.findById(kioskUpdateRequest.readingRoomId).orElseThrow {
            NotFoundException("독서실")
        }
        kiosk.name = kioskUpdateRequest.name
        kiosk.readingRoom = readingRoom
    }

    fun deleteKiosk(kioskId: Long) {
        val kiosk = kioskRepository.findById(kioskId).orElseThrow {
            NotFoundException("키오스크")
        }
        kioskRepository.delete(kiosk)
    }

    fun findKiosks(): List<KioskResponse> = kioskRepository.findAll().map {
        KioskResponse(id = it.id!!, name = it.name, readingRoomName = it.readingRoom.name)
    }

    fun saveEntryDevice(entryDeviceCreateRequest: EntryDeviceCreateRequest) {
        if (entryDeviceRepository.existsByName(entryDeviceCreateRequest.name)) {
            throw AlreadyExistsException("출입 디바이스 관리명")
        }

        val parentKiosk = entryDeviceCreateRequest.parentKioskId?.let {
            kioskRepository.findById(it).orElseThrow {
                NotFoundException("연동할 키오스크")
            }
        }

        val lectureRoom = entryDeviceCreateRequest.lectureRoomId?.let {
            lectureRoomRepository.findById(it).orElseThrow {
                NotFoundException("강의실")
            }
        }

        val entryDevice = EntryDevice(
            name = entryDeviceCreateRequest.name,
            accessType = entryDeviceCreateRequest.accessType,
            kiosk = parentKiosk,
            lectureRoom = lectureRoom
        )
        entryDeviceRepository.save(entryDevice)
    }

    fun findEntryDevices(): List<EntryDeviceResponse> = entryDeviceRepository.findAll().map {
        EntryDeviceResponse(
            id = it.id!!,
            name = it.name,
            accessType = it.accessType,
            parentKioskName = it.kiosk?.name,
            lectureRoomName = it.lectureRoom?.name
        )
    }

    fun findEntryDevice(deviceId: Long): EntryDeviceResponse =
        entryDeviceRepository.findById(deviceId).map {
            EntryDeviceResponse(
                id = it.id!!,
                name = it.name,
                accessType = it.accessType,
                parentKioskName = it.kiosk?.name,
                lectureRoomName = it.lectureRoom?.name
            )
        }.orElseThrow { NotFoundException("디바이스 정보") }


}