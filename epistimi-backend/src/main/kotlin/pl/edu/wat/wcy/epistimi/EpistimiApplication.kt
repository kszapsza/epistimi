package pl.edu.wat.wcy.epistimi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import java.util.TimeZone
import javax.annotation.PostConstruct

@SpringBootApplication
class EpistimiApplication {
    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"))
    }

    @Bean
    fun restTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<EpistimiApplication>(*args)
}
