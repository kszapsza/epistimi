package pl.edu.wat.wcy.epistimi.teacher.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherAggregator
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetailsDecorator
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class TeacherConfiguration {

    @Bean
    fun teacherFacade(
        teacherAggregator: TeacherAggregator,
        teacherRegistrar: TeacherRegistrar,
        detailsDecorator: TeacherDetailsDecorator,
    ): TeacherFacade {
        return TeacherFacade(
            teacherAggregator,
            teacherRegistrar,
            detailsDecorator,
        )
    }

    @Bean
    fun teacherAggregator(
        organizationContextProvider: OrganizationContextProvider,
        teacherRepository: TeacherRepository,
    ): TeacherAggregator {
        return TeacherAggregator(
            organizationContextProvider,
            teacherRepository,
        )
    }

    @Bean
    fun teacherRegistrar(
        teacherRepository: TeacherRepository,
        userRegistrar: UserRegistrar,
        organizationContextProvider: OrganizationContextProvider,
    ): TeacherRegistrar {
        return TeacherRegistrar(
            teacherRepository,
            userRegistrar,
            organizationContextProvider,
        )
    }

    @Bean
    fun teacherDetailsDecorator(
        userRepository: UserRepository,
        organizationRepository: OrganizationRepository,
    ): TeacherDetailsDecorator {
        return TeacherDetailsDecorator(
            userRepository,
            organizationRepository,
        )
    }
}
