package pl.edu.wat.wcy.epistimi.parent.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentRegistrationService
import pl.edu.wat.wcy.epistimi.parent.domain.port.ParentRepository
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

@Configuration
class ParentConfiguration {
    @Bean
    fun parentFacade(
        parentRegistrationService: ParentRegistrationService,
    ): ParentFacade {
        return ParentFacade(parentRegistrationService)
    }

    @Bean
    fun parentRegistrationService(
        parentRepository: ParentRepository,
        userRegistrationService: UserRegistrationService,
    ): ParentRegistrationService {
        return ParentRegistrationService(
            parentRepository,
            userRegistrationService,
        )
    }
}
