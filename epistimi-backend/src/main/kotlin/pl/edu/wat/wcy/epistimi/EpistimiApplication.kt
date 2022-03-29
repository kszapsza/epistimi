package pl.edu.wat.wcy.epistimi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class EpistimiApplication {
    @Bean
    fun restTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<EpistimiApplication>(*args)
}
