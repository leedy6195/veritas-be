package com.oxingaxin.veritas.lecture.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.lecture.domain.dto.LectureRequest
import com.oxingaxin.veritas.lecture.domain.dto.LectureResponse
import com.oxingaxin.veritas.lecture.domain.dto.ScheduleRequest
import com.oxingaxin.veritas.lecture.domain.entity.Enrollment
import com.oxingaxin.veritas.lecture.domain.entity.Lecture
import com.oxingaxin.veritas.lecture.domain.entity.Schedule
import com.oxingaxin.veritas.lecture.domain.entity.ScheduleAttendance
import com.oxingaxin.veritas.lecture.service.EnrollmentService
import com.oxingaxin.veritas.lecture.service.LectureService
import com.oxingaxin.veritas.lecture.service.ScheduleAttendanceService
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
    private val lectureService: LectureService,
    private val scheduleAttendanceService: ScheduleAttendanceService,
    private val enrollmentService: EnrollmentService

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
    fun getLectures(): BaseResponse<List<LectureResponse>> {
        val lectures = lectureService.findLectures()
        return BaseResponse.ok(lectures)
    }

    @GetMapping("/{lectureId}")
    fun getLecture(
            @PathVariable lectureId: Long
    ): BaseResponse<Lecture> {
        val lecture = lectureService.findLecture(lectureId)
        return BaseResponse.ok(lecture)
    }

    @GetMapping("/{lectureId}/schedules")
    fun getSchedules(
            @PathVariable lectureId: Long
    ): BaseResponse<List<Schedule>> {
        val schedules = lectureService.findSchedules(lectureId)
        return BaseResponse.ok(schedules)
    }

    @PostMapping("/{lectureId}/schedules")
    fun createSchedule(
            @PathVariable lectureId: Long,
            @RequestBody scheduleRequest: ScheduleRequest
    ): BaseResponse<Unit> {
        lectureService.saveSchedule(lectureId, scheduleRequest)
        return BaseResponse.ok()
    }

    @GetMapping("/{lectureId}/scheduleAttendances")
    fun getScheduleAttendances(
            @PathVariable lectureId: Long
    ): BaseResponse<List<ScheduleAttendance>> {
        val scheduleAttendances = scheduleAttendanceService.findScheduleAttendances(lectureId)
        return BaseResponse.ok(scheduleAttendances)
    }

    @GetMapping("/{lectureId}/enrollments}")
    fun getEnrollments(
            @PathVariable lectureId: Long
    ): BaseResponse<List<Enrollment>> {
        val enrollments = enrollmentService.findEnrollmentsByLectureId(lectureId)
        return BaseResponse.ok(enrollments)
    }

}