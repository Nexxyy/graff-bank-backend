package com.ledger.nexy

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<LedgerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
