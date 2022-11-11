package pl.edu.wat.wcy.epistimi.subject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.port.CourseRepository
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.subject.domain.access.SubjectAccessValidator
import pl.edu.wat.wcy.epistimi.subject.domain.port.SubjectRepository
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectAggregatorService
import pl.edu.wat.wcy.epistimi.subject.domain.service.SubjectRegisterService
import pl.edu.wat.wcy.epistimi.teacher.port.TeacherRepository

@Configuration
class SubjectConfiguration {
    @Bean
    fun subjectFacade(
        subjectAggregatorService: SubjectAggregatorService,
        subjectRegisterService: SubjectRegisterService,
    ): SubjectFacade {
        return SubjectFacade(
            subjectAggregatorService,
            subjectRegisterService
        )
    }

    @Bean
    fun subjectAggregatorService(
        subjectRepository: SubjectRepository,
        subjectAccessValidator: SubjectAccessValidator,
    ): SubjectAggregatorService {
        return SubjectAggregatorService(
            subjectRepository,
            subjectAccessValidator,
        )
    }

    @Bean
    fun subjectRegisterService(
        subjectRepository: SubjectRepository,
        courseRepository: CourseRepository,
        teacherRepository: TeacherRepository,
    ): SubjectRegisterService {
        return SubjectRegisterService(
            subjectRepository,
            courseRepository,
            teacherRepository,
        )
    }

    @Bean
    fun subjectAccessValidator(): SubjectAccessValidator {
        return SubjectAccessValidator()
    }
}
