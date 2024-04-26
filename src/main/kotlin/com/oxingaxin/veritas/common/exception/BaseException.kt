package com.oxingaxin.veritas.common.exception

open class BaseException(message: String, val status: Int)
    : RuntimeException(message)