package com.oxingaxin.veritas.common.middleware


import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.common.exception.BaseException
import org.springframework.http.HttpStatus


import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): BaseResponse<Void>
        = BaseResponse(BaseResponse.Header(false, ex.status, ex.message))

    @ExceptionHandler(RuntimeException::class, IllegalArgumentException::class)
    fun handleRuntimeException(ex: RuntimeException): BaseResponse<Void>
        = BaseResponse(BaseResponse.Header(false, HttpStatus.BAD_REQUEST.value(), ex.message))

}