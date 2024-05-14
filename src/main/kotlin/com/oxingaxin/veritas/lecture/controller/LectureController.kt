package com.oxingaxin.veritas.lecture.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.lecture.domain.dto.LectureRequest
import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import com.oxingaxin.veritas.lecture.service.LectureService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
            @RequestBody lectureRequest: LectureRequest
    ): BaseResponse<Unit> {
        lectureService.saveLecture(lectureRequest)

        return BaseResponse.ok()
    }

    @PutMapping("/{lectureId}")
    fun updateLecture(
            @PathVariable lectureId: Long,
            @RequestBody lectureRequest: LectureRequest

    ): BaseResponse<Unit> {
        lectureService.updateLecture(lectureId, lectureRequest)
        return BaseResponse.ok()

    }

    @DeleteMapping("/{lectureId}")
    fun deleteLecture(
            @PathVariable lectureId: Long
    ): BaseResponse<Unit> {
        lectureService.deleteLecture(lectureId)
        return BaseResponse.ok()
    }

    @GetMapping
    fun getLectures(): BaseResponse<List<Lecture>> {
        val lectures = lectureService.findLectures()
        return BaseResponse.ok(lectures)
    }

}