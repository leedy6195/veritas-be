package com.oxingaxin.veritas.common

import org.springframework.http.HttpStatus

data class BaseResponse<T>(
        val header: Header = Header(),
        val data: T? = null
) {
    data class Header(
            val success: Boolean = false,
            val status: Int = 0,
            val message: String? = null
    )


    class Builder<T> {
        private var success: Boolean = false
        private var status: Int = 0
        private var message: String? = null
        private var data: T? = null

        fun success(success: Boolean) = apply { this.success = success }
        fun status(status: Int) = apply { this.status = status }
        fun message(message: String?) = apply { this.message = message }
        fun data(data: T?) = apply { this.data = data }

        fun build(): BaseResponse<T> {
            val header = Header(success, status, message)
            return BaseResponse(header, data)
        }
    }

    companion object {
        fun <T> builder(): Builder<T> {
            return Builder()
        }

        fun <T> error(status: Int, message: String): BaseResponse<T> =
            builder<T>()
                .success(false)
                .status(status)
                .message(message)
                .build()

        fun <T> ok(data: T? = null): BaseResponse<T> =
            builder<T>()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(data)
                .build()

        fun <T> created(data: T? = null): BaseResponse<T> =
            builder<T>()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("created successfully")
                .data(data)
                .build()

        fun <T> deleted(data: T? = null): BaseResponse<T> =
            builder<T>()
                .success(true)
                .status(HttpStatus.NO_CONTENT.value())
                .message("deleted successfully")
                .data(data)
                .build()
    }
}
