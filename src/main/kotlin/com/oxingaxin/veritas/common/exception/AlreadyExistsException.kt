package com.oxingaxin.veritas.common.exception

import org.springframework.http.HttpStatus

class AlreadyExistsException(entity: String): BaseException(
    "이미 존재하는 $entity 입니다",
    HttpStatus.CONFLICT.value()
) {
    constructor(entity: String, field: String, value: String) : this("$entity ($field: $value)")
}