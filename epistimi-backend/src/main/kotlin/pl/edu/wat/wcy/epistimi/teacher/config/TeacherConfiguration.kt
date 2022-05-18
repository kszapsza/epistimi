package pl.edu.wat.wcy.epistimi.teacher.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.teacher.TeacherDetailsDecorator
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class TeacherConfiguration {

    @Bean
    fun teacherFacade(
        teacherRepository: TeacherRepository,
        organizationContextProvider: OrganizationContextProvider,
        detailsDecorator: TeacherDetailsDecorator,
    ): TeacherFacade {
        return TeacherFacade(
            teacherRepository,
            organizationContextProvider,
            detailsDecorator,
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
