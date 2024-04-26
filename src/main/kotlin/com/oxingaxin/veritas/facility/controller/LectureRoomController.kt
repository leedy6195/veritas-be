package com.oxingaxin.veritas.facility.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.facility.domain.dto.LectureRoomCreateRequest
import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import com.oxingaxin.veritas.facility.service.LectureRoomService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lecturerooms")
class LectureRoomController(
    private val lectureRoomService: LectureRoomService
) {

    @PostMapping
    fun createLectureRoom(
            @RequestBody lectureRoomCreateRequest: LectureRoomCreateRequest
    ): BaseResponse<Void> {
        lectureRoomService.createLectureRoom(lectureRoomCreateRequest)
        return BaseResponse.ok()
    }

    @GetMapping
    fun getLectureRooms() : BaseResponse<List<LectureRoom>> {
        val lectureRooms = lectureRoomService.findLectureRooms()
        return BaseResponse.ok(lectureRooms)
    }

    @DeleteMapping("/{id}")
    fun deleteLectureRoom(
        @PathVariable id: Long
    ): BaseResponse<Void> {
        lectureRoomService.deleteLectureRoom(id)
        return BaseResponse.ok()
    }

    @PutMapping("/{id}")
    fun updateLectureRoom(
        @PathVariable id: Long,
        @RequestBody lectureRoomCreateRequest: LectureRoomCreateRequest
    ): BaseResponse<Void> {
        lectureRoomService.updateLectureRoom(id, lectureRoomCreateRequest)
        return BaseResponse.ok()
    }

}