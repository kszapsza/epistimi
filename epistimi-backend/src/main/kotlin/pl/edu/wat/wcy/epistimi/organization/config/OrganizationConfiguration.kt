package pl.edu.wat.wcy.epistimi.organization.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationDetailsDecorator
import pl.edu.wat.wcy.epistimi.organization.OrganizationFacade
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class OrganizationConfiguration {

    @Bean
    fun organizationFacade(
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
        locationClient: OrganizationLocationClient,
        detailsDecorator: OrganizationDetailsDecorator,
    ): OrganizationFacade {
        return OrganizationFacade(
            organizationRepository,
            userRepository,
            locationClient,
            detailsDecorator,
        )
    }

    @Bean
    fun organizationDetailsDecorator(userRepository: UserRepository): OrganizationDetailsDecorator {
        return OrganizationDetailsDecorator(userRepository)
    }

    @Bean
    fun organizationContextProvider(
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
        teacherRepository: TeacherRepository,
    ): OrganizationContextProvider {
        return OrganizationContextProvider(
            organizationRepository,
            userRepository,
            teacherRepository,
        )
    }
}
