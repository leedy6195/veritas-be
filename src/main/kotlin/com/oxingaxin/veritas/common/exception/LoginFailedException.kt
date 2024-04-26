package com.oxingaxin.veritas.common.exception

import org.springframework.http.HttpStatus

class LoginFailedException: BaseException(
    "invalid loginId or password",
    HttpStatus.UNAUTHORIZED.value()
)