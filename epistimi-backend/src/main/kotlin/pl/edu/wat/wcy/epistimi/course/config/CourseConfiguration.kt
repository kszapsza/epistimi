package pl.edu.wat.wcy.epistimi.course.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.CourseAggregator
import pl.edu.wat.wcy.epistimi.course.CourseDetailsDecorator
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.CourseRegistrar
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.organization.port.OrganizationRepository
import pl.edu.wat.wcy.epistimi.parent.port.ParentRepository
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.user.port.UserRepository

@Configuration
class CourseConfiguration {

    @Bean
    fun courseFacade(
        courseAggregator: CourseAggregator,
        courseRegistrar: CourseRegistrar,
        courseRepository: CourseRepository,
        detailsDecorator: CourseDetailsDecorator,
    ): CourseFacade {
        return CourseFacade(
            courseAggregator,
            courseRegistrar,
            courseRepository,
            detailsDecorator,
        )
    }

    @Bean
    fun courseAggregator(
        courseRepository: CourseRepository,
        organizationContextProvider: OrganizationContextProvider,
    ): CourseAggregator {
        return CourseAggregator(
            courseRepository,
            organizationContextProvider,
        )
    }

    @Bean
    fun courseRegistrar(
        courseRepository: CourseRepository,
        teacherRepository: TeacherRepository,
        organizationContextProvider: OrganizationContextProvider,
    ): CourseRegistrar {
        return CourseRegistrar(
            courseRepository,
            teacherRepository,
            organizationContextProvider,
        )
    }

    @Bean
    fun courseDetailsDecorator(
        teacherRepository: TeacherRepository,
        organizationRepository: OrganizationRepository,
        userRepository: UserRepository,
        studentRepository: StudentRepository,
        parentRepository: ParentRepository,
    ): CourseDetailsDecorator {
        return CourseDetailsDecorator(
            teacherRepository,
            organizationRepository,
            userRepository,
            studentRepository,
            parentRepository,
        )
    }
}
