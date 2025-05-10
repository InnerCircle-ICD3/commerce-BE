package com.fastcampus.commerce.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.fastcampus.commerce"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
