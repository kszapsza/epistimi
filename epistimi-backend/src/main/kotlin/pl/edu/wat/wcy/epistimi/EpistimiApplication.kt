package pl.edu.wat.wcy.epistimi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone
import javax.annotation.PostConstruct

@SpringBootApplication
class EpistimiApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EpistimiApplication>(*args)
        }
    }

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"))
    }
}
