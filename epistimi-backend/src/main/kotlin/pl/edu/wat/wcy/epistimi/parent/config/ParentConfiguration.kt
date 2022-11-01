package pl.edu.wat.wcy.epistimi.parent.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar

@Configuration
class ParentConfiguration {

    @Bean
    fun parentRegistrar(
        parentRepository: ParentRepository,
        userRegistrar: UserRegistrar,
    ): ParentRegistrar {
        return ParentRegistrar(
            parentRepository,
            userRegistrar,
        )
    }
}
