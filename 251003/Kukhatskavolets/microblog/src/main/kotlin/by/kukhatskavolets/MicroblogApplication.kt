package by.kukhatskavolets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MicroblogApplication

fun main(args: Array<String>) {
    runApplication<MicroblogApplication>(*args)
}
