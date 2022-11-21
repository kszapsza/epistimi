package pl.edu.wat.wcy.epistimi.parent.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.parent.domain.port.ParentRepository
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentAggregatorService
import pl.edu.wat.wcy.epistimi.parent.domain.service.ParentRegistrationService
import pl.edu.wat.wcy.epistimi.user.domain.service.UserRegistrationService

@Configuration
class ParentConfiguration {
    @Bean
    fun parentFacade(
        parentAggregatorService: ParentAggregatorService,
        parentRegistrationService: ParentRegistrationService,
    ): ParentFacade {
        return ParentFacade(
            parentAggregatorService,
            parentRegistrationService,
        )
    }

    @Bean
    fun parentAggregatorService(
        parentRepository: ParentRepository,
    ): ParentAggregatorService {
        return ParentAggregatorService(parentRepository)
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
