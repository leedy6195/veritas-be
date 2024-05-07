package com.oxingaxin.veritas.facility.controller


import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.facility.domain.dto.*
import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import com.oxingaxin.veritas.facility.service.ReadingRoomService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/readingrooms")
class ReadingRoomController(
 private val readingRoomService: ReadingRoomService,
){
    //private val emitters = ConcurrentHashMap<String, SseEmitter>()

    @PostMapping
    fun createReadingRoom(
            @RequestBody readingRoomCreateRequest: ReadingRoomCreateRequest
    ): BaseResponse<ReadingRoomCreateResponse> {

        val readingRoomCreateResponse = readingRoomService.saveReadingRoom(readingRoomCreateRequest)
        return BaseResponse.created(readingRoomCreateResponse)
    }

    @PutMapping("/{roomId}")
    fun updateReadingRoom(
            @PathVariable roomId: Long,
            @RequestBody readingRoomUpdateRequest: ReadingRoomUpdateRequest
    ): BaseResponse<Void> {
        readingRoomService.updateReadingRoom(roomId, readingRoomUpdateRequest)
        return BaseResponse.ok()
    }

    @DeleteMapping("/{roomId}")
    fun deleteReadingRoom(
            @PathVariable roomId: Long
    ): BaseResponse<Void> {
        readingRoomService.deleteReadingRoom(roomId)
        return BaseResponse.deleted()
    }

    @GetMapping
    fun getReadingRooms(): BaseResponse<List<ReadingRoom>> {
        val readingRooms = readingRoomService.findReadingRooms()
        return BaseResponse.ok(readingRooms)
    }

    @GetMapping("/{roomId}")
    fun getReadingRoom(
            @PathVariable roomId: Long
    ): BaseResponse<ReadingRoom> {
        val readingRoom = readingRoomService.findReadingRoomById(roomId)
        return BaseResponse.ok(readingRoom)
    }

    @PostMapping("/{roomId}/seats")
    fun createSeat(
            @PathVariable roomId: Long,
            @RequestBody seatCreateRequest: SeatCreateRequest
    ): BaseResponse<SeatCreateResponse> {
        val seatCreateResponse = readingRoomService.saveSeat(roomId, seatCreateRequest)
        return BaseResponse.created(seatCreateResponse)
    }
    @DeleteMapping("/{roomId}/seats/{seatId}")
    fun deleteSeat(
            @PathVariable seatId: Long
    ): BaseResponse<Void> {
        readingRoomService.deleteSeat(seatId)
        return BaseResponse.deleted()
    }

    /*
    @GetMapping("/{roomId}/seats/status", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun seatUpdates(@PathVariable roomId: Long): SseEmitter {
        val emitter = SseEmitter()

        emitter.onCompletion {
            emitters.remove(emitter.toString())
        }
        emitter.onTimeout { emitter.complete() }



        emitters[emitter.toString()] = emitter
        return emitter
    }

     */


    @PutMapping("/{roomId}/seats/{seatId}")
    fun updateSeat(
            @PathVariable roomId: String,
            @PathVariable seatId: Long,
            @RequestBody seatUpdateRequest: SeatUpdateRequest
    ): BaseResponse<SeatUpdateResponse> {
        val seatUpdateResponse = readingRoomService.updateSeat(seatId, seatUpdateRequest)
        /*
        emitters.forEach { (_, emitter) ->
            try {

                emitter.send(SseEmitter.event().name("seatUpdate").data(seatUpdateResponse))
            } catch (e: Exception) {
                emitters.remove(emitter.toString())
            }
        }

         */
        return BaseResponse.ok(seatUpdateResponse)
    }

}