package com.oxingaxin.veritas.lecture.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.lecture.domain.dto.LectureCreateRequest
import com.oxingaxin.veritas.lecture.service.LectureService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lectures")
class LectureController(
    private val lectureService: LectureService
) {
    @PostMapping
    fun createLecture(
            @RequestBody lectureCreateRequest: LectureCreateRequest
    ): BaseResponse<Unit> {
        lectureService.saveLecture(lectureCreateRequest)

        return BaseResponse.ok()
    }

}