package com.oxingaxin.veritas.common.util

import com.oxingaxin.veritas.facility.domain.entity.LectureRoom
import com.oxingaxin.veritas.facility.domain.entity.ReadingRoom
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ConcurrentHashMap

@Component
class ReceiverUtil() {
    val restTemplate = RestTemplate()

    companion object {
        val mutexMap = ConcurrentHashMap<String, Int>()
    }

    fun openLectureRoomDoor(lectureRoom: LectureRoom) {
        restTemplate.exchange(
            "https://blynk.cloud/external/api/update?token=${lectureRoom.receiverToken}&v0=1",
            HttpMethod.GET,
            null,
            String::class.java
        )
    }

    fun openDoor(readingRoom: ReadingRoom) {
        restTemplate.exchange(
            "https://blynk.cloud/external/api/update?token=${readingRoom.receiverToken}&v0=0",
            HttpMethod.GET,
            null,
            String::class.java
        )
    }

    fun closeDoor(readingRoom: ReadingRoom) {
        restTemplate.exchange(
            "https://blynk.cloud/external/api/update?token=${readingRoom.receiverToken}&v0=1",
            HttpMethod.GET,
            null,
            String::class.java
        )

    }
}