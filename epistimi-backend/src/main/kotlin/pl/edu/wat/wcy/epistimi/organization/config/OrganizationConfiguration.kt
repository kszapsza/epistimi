package pl.edu.wat.wcy.epistimi.organization.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationFacade
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.domain.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.organization.domain.service.OrganizationRegistrationService
import pl.edu.wat.wcy.epistimi.user.UserFacade

@Configuration
class OrganizationConfiguration {

    @Bean
    fun organizationFacade(
        organizationRegistrationService: OrganizationRegistrationService,
        organizationRepository: OrganizationRepository,
        locationClient: OrganizationLocationClient,
    ): OrganizationFacade {
        return OrganizationFacade(
            organizationRegistrationService,
            organizationRepository,
            locationClient,
        )
    }

    @Bean
    fun organizationRegistrationService(
        organizationRepository: OrganizationRepository,
        userFacade: UserFacade,
        locationClient: OrganizationLocationClient,
    ): OrganizationRegistrationService {
        return OrganizationRegistrationService(
            organizationRepository,
            userFacade,
            locationClient,
        )
    }
}
