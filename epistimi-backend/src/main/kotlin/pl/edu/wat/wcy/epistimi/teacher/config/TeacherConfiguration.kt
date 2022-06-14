package pl.edu.wat.wcy.epistimi.teacher.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.teacher.TeacherAggregator
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherRegistrar
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar

@Configuration
class TeacherConfiguration {

    @Bean
    fun teacherFacade(
        teacherAggregator: TeacherAggregator,
        teacherRegistrar: TeacherRegistrar,
    ): TeacherFacade {
        return TeacherFacade(
            teacherAggregator,
            teacherRegistrar,
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
}
