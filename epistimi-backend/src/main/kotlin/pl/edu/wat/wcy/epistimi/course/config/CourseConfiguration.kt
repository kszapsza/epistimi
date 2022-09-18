package pl.edu.wat.wcy.epistimi.course.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.CourseAggregator
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.CourseRegistrar
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.organization.OrganizationContextProvider
import pl.edu.wat.wcy.epistimi.student.port.StudentRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

@Configuration
class CourseConfiguration {

    @Bean
    fun courseFacade(
        courseAggregator: CourseAggregator,
        courseRegistrar: CourseRegistrar,
        courseRepository: CourseRepository,
        studentRepository: StudentRepository,
    ): CourseFacade {
        return CourseFacade(
            courseAggregator,
            courseRegistrar,
            courseRepository,
            studentRepository,
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
}
