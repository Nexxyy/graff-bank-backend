package com.ledger.nexy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class LedgerApplication

fun main(args: Array<String>) {
    runApplication<LedgerApplication>(*args)
}
