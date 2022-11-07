package pl.edu.wat.wcy.epistimi.course.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.CourseAggregator
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.course.CourseRegistrar
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

@Configuration
class CourseConfiguration {

    @Bean
    fun courseFacade(
        courseAggregator: CourseAggregator,
        courseRegistrar: CourseRegistrar,
    ): CourseFacade {
        return CourseFacade(
            courseAggregator,
            courseRegistrar,
        )
    }

    @Bean
    fun courseAggregator(
        courseRepository: CourseRepository,
    ): CourseAggregator {
        return CourseAggregator(
            courseRepository,
        )
    }

    @Bean
    fun courseRegistrar(
        courseRepository: CourseRepository,
        teacherRepository: TeacherRepository,
    ): CourseRegistrar {
        return CourseRegistrar(
            courseRepository,
            teacherRepository,
        )
    }
}
