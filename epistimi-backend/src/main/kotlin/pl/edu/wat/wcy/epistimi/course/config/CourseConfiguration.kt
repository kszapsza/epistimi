package pl.edu.wat.wcy.epistimi.course.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseAggregatorService
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.domain.service.CourseRegistrationService
import pl.edu.wat.wcy.epistimi.course.domain.port.CourseRepository
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository

@Configuration
class CourseConfiguration {
    @Bean
    fun courseFacade(
        courseAggregatorService: CourseAggregatorService,
        courseRegistrationService: CourseRegistrationService,
    ): CourseFacade {
        return CourseFacade(
            courseAggregatorService,
            courseRegistrationService,
        )
    }

    @Bean
    fun courseAggregatorService(
        courseRepository: CourseRepository,
    ): CourseAggregatorService {
        return CourseAggregatorService(
            courseRepository,
        )
    }

    @Bean
    fun courseRegistrationService(
        courseRepository: CourseRepository,
        teacherRepository: TeacherRepository,
    ): CourseRegistrationService {
        return CourseRegistrationService(
            courseRepository,
            teacherRepository,
        )
    }
}
