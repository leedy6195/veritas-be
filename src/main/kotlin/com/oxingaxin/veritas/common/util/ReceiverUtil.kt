package com.oxingaxin.veritas.common.util

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

    fun openDoor(readingRoom: ReadingRoom): Boolean {
        mutexMap[readingRoom.receiverToken] = mutexMap.getOrDefault(readingRoom.receiverToken, 0) + 1

        if (mutexMap.getOrDefault(readingRoom.receiverToken, 0) > 1) {
            return false
        }



        restTemplate.exchange(
                "https://blynk.cloud/external/api/update?token=${readingRoom.receiverToken}&v0=0",
                HttpMethod.GET,
                null,
                String::class.java
        )
        // 10초 후 문을 닫음
        Thread.sleep(10000)

        restTemplate.exchange(
                "https://blynk.cloud/external/api/update?token=${readingRoom.receiverToken}&v0=1",
                HttpMethod.GET,
                null,
                String::class.java
        )
        return true
    }


}