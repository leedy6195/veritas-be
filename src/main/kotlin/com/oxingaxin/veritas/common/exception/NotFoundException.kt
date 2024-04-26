package com.oxingaxin.veritas.common.exception

import org.springframework.http.HttpStatus

class NotFoundException(entity: String) : BaseException(
    "존재하지 않는 $entity 입니다",
    HttpStatus.NOT_FOUND.value()
){
    constructor(entity: String, field: String, value: String) : this("$entity($field: $value)")
}