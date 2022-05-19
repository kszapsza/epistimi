package pl.edu.wat.wcy.epistimi.student.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.parent.ParentRegistrar
import pl.edu.wat.wcy.epistimi.student.StudentRegistrar
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.user.UserRegistrar

@Configuration
class StudentConfiguration {

    @Bean
    fun studentRegistrar(
        studentRepository: StudentRepository,
        userRegistrar: UserRegistrar,
        parentRegistrar: ParentRegistrar,
        courseFacade: CourseFacade,
        organizationContextProvider: OrganizationContextProvider,
    ): StudentRegistrar {
        return StudentRegistrar(
            studentRepository,
            userRegistrar,
            parentRegistrar,
            courseFacade,
            organizationContextProvider,
        )
    }
}
