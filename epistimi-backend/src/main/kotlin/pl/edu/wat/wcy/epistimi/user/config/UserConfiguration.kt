package pl.edu.wat.wcy.epistimi.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.wat.wcy.epistimi.user.UserAggregator
import pl.edu.wat.wcy.epistimi.user.UserCredentialsGenerator
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class UserConfiguration {

    @Bean
    fun userAggregator(userRepository: UserRepository): UserAggregator {
        return UserAggregator(userRepository)
    }

    @Bean
    fun userRegistrar(
        userRepository: UserRepository,
        credentialsGenerator: UserCredentialsGenerator,
        passwordEncoder: PasswordEncoder,
    ): UserRegistrar {
        return UserRegistrar(
            userRepository,
            credentialsGenerator,
            passwordEncoder,
        )
    }
}
