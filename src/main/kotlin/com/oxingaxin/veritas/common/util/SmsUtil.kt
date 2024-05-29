package com.oxingaxin.veritas.common.util

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class SmsUtil {
    val restTemplate = RestTemplate()

    companion object {
        const val SMS_API_URL = "https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:331793620405:veritas-s/messages"
        const val SMS_API_KEY = "jbxHiVXxi4zalY7cWyzK"
        const val SMS_API_SECRET = "Lt0LVogXnP3XdjYQzcAFravH4Sug6mORxksbV4OW"
    }

    fun sendSms(smsRequest: SmsRequest) {
        val timestamp = System.currentTimeMillis().toString()
        val signature = makeSignature(timestamp)

        /*
        val headers = mapOf(
            "Content-Type" to "application/json; charset=utf-8",
            "x-ncp-apigw-timestamp" to timestamp,
            "x-ncp-iam-access-key" to SMS_API_KEY,
            "x-ncp-apigw-signature-v2" to signature
        )

         */
        val headers: MultiValueMap<String, String> = LinkedMultiValueMap()
        headers.add("Content-Type", "application/json; charset=utf-8")
        headers.add("x-ncp-apigw-timestamp", timestamp)
        headers.add("x-ncp-iam-access-key", SMS_API_KEY)
        headers.add("x-ncp-apigw-signature-v2", signature)

        val body = mapOf(
            "type" to "SMS",
            "contentType" to "COMM",
            "from" to "025645557",
            "content" to smsRequest.message,
            "messages" to listOf(
                mapOf(
                    "to" to smsRequest.to
                )
            )
        )
        restTemplate.exchange(SMS_API_URL, HttpMethod.POST, HttpEntity(body, headers), String::class.java)
        //restTemplate.postForObject(SMS_API_URL, body, String::class.java, headers)
    }

    fun convertMessage(memberName: String): String {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("MM월 dd일")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val formattedDate = currentDateTime.format(dateFormatter)
        val formattedTime = currentDateTime.format(timeFormatter)

        return "[베리타스S 등원 안내]\n" +
                "$formattedDate ${memberName}학생이 $formattedTime 등원하여 안내드립니다."


    }

    fun convertTel(tel: String): String {
        return tel.replace("-", "")
    }

    fun makeSignature(timestamp: String): String {
        val space = " "
        val newLine = "\n"
        val method = "POST"
        val url = "/sms/v2/services/ncp:sms:kr:331793620405:veritas-s/messages"

        val message = StringBuilder()
            .append(method)
            .append(space)
            .append(url)
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(SMS_API_KEY)
            .toString()

        val signingKey = SecretKeySpec(SMS_API_SECRET.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val hmacSha256 = Mac.getInstance("HmacSHA256")

        hmacSha256.init(signingKey)
        val rawHmac = hmacSha256.doFinal(message.toByteArray(Charsets.UTF_8))
        val encodeBase64String = Base64.encodeBase64String(rawHmac)

        return encodeBase64String
    }
}

data class SmsRequest(
    val to: String,
    val message: String
)