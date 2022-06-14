package pl.edu.wat.wcy.epistimi.organization.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.OrganizationFacade
import pl.edu.wat.wcy.epistimi.organization.OrganizationRegistrar
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationLocationClient
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class OrganizationConfiguration {

    @Bean
    fun organizationFacade(
        organizationRegistrar: OrganizationRegistrar,
        organizationRepository: OrganizationRepository,
        locationClient: OrganizationLocationClient,
    ): OrganizationFacade {
        return OrganizationFacade(
            organizationRegistrar,
            organizationRepository,
            locationClient,
        )
    }

    @Bean
    fun organizationRegistrar(
        organizationRepository: OrganizationRepository,
        userRegistrar: UserRegistrar,
        locationClient: OrganizationLocationClient,
    ): OrganizationRegistrar {
        return OrganizationRegistrar(
            organizationRepository,
            userRegistrar,
            locationClient,
        )
    }

    @Bean
    fun organizationContextProvider(
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
        teacherRepository: TeacherRepository,
        studentRepository: StudentRepository,
        parentRepository: ParentRepository,
    ): OrganizationContextProvider {
        return OrganizationContextProvider(
            organizationRepository,
            userRepository,
            teacherRepository,
            studentRepository,
            parentRepository,
        )
    }
}
