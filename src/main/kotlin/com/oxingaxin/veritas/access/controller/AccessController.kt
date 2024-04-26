package com.oxingaxin.veritas.access.controller

import com.oxingaxin.veritas.access.domain.dto.*
import com.oxingaxin.veritas.access.service.AccessService
import com.oxingaxin.veritas.common.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/access")
class AccessController(
        private val accessService: AccessService
) {

    @GetMapping
    fun getAttendances(): BaseResponse<List<AttendanceResponse>> {
        val response = accessService.findAttendances()
        return BaseResponse.ok(response)
    }

    @PostMapping("/readingroom/enter")
    fun enterReadingRoom(
            @RequestBody readingRoomAccessCreateRequest: ReadingRoomAccessCreateRequest
    ): BaseResponse<ReadingRoomAccessCreateResponse> {
        val response =
            accessService.saveReadingRoomAccess(readingRoomAccessCreateRequest)
        return BaseResponse.ok(response)
    }

    @PostMapping("/readingroom/exit")
    fun exitReadingRoom(
            @RequestBody readingRoomExitRequest: ReadingRoomExitRequest
    ): BaseResponse<ReadingRoomExitResponse> {
        val response = accessService.exitReadingRoom(readingRoomExitRequest)
        return BaseResponse.ok(response)
    }


    @PostMapping("/readingroom/check")
    fun checkReadingRoomAccess(
            @RequestBody readingRoomAccessCheckRequest: ReadingRoomAccessCheckRequest
    ): BaseResponse<ReadingRoomAccessCheckResponse?> {
        val response =
            accessService.findTodaySeatId(readingRoomAccessCheckRequest)
        return BaseResponse.ok(response)
    }

    @PostMapping("/lectureroom/enter")
    fun enterLectureRoom(
            @RequestBody lectureRoomAccessRequest: LectureRoomAccessRequest
    ): BaseResponse<LectureRoomAccessResponse> {
        val response = accessService.accessLectureRoom(lectureRoomAccessRequest)
        return BaseResponse.ok(response)
    }

    @PostMapping("/lectureroom/exit")
    fun exitLectureRoom(
            @RequestBody lectureRoomAccessRequest: LectureRoomAccessRequest
    ): BaseResponse<LectureRoomAccessResponse> {
        val response = accessService.accessLectureRoom(lectureRoomAccessRequest)
        return BaseResponse.ok(response)
    }
}