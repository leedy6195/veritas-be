package com.oxingaxin.veritas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class VeritasApplication

fun main(args: Array<String>) {
	runApplication<VeritasApplication>(*args)
}
