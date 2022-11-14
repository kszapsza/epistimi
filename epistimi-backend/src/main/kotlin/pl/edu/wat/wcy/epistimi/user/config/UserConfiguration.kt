package pl.edu.wat.wcy.epistimi.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.wat.wcy.epistimi.user.UserFacade
import pl.edu.wat.wcy.epistimi.user.domain.port.UserRepository
import pl.edu.wat.wcy.epistimi.user.domain.service.UserAggregatorService
import pl.edu.wat.wcy.epistimi.user.domain.service.UserCredentialsGenerator
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

@Configuration
class UserConfiguration {
    @Bean
    fun userFacade(
        userAggregatorService: UserAggregatorService,
        userRegistrationService: UserRegistrationService,
    ): UserFacade {
        return UserFacade(
            userAggregatorService,
            userRegistrationService,
        )
    }

    @Bean
    fun userAggregatorService(
        userRepository: UserRepository
    ): UserAggregatorService {
        return UserAggregatorService(userRepository)
    }

    @Bean
    fun userRegistrationService(
        userRepository: UserRepository,
        credentialsGenerator: UserCredentialsGenerator,
        passwordEncoder: PasswordEncoder,
    ): UserRegistrationService {
        return UserRegistrationService(
            userRepository,
            credentialsGenerator,
            passwordEncoder,
        )
    }

    @Bean
    fun userCredentialsGenerator(
        userRepository: UserRepository,
    ): UserCredentialsGenerator {
        return UserCredentialsGenerator(userRepository)
    }
}
