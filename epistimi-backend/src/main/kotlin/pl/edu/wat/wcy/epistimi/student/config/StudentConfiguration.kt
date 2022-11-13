package pl.edu.wat.wcy.epistimi.student.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.course.CourseFacade
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.student.domain.access.StudentAccessValidator
import pl.edu.wat.wcy.epistimi.student.domain.port.StudentRepository
import pl.edu.wat.wcy.epistimi.student.domain.service.StudentAggregatorService
import pl.edu.wat.wcy.epistimi.student.domain.service.StudentRegistrationService
import pl.edu.wat.wcy.epistimi.user.UserFacade

@Configuration
class StudentConfiguration {

    @Bean
    fun studentFacade(
        studentAggregatorService: StudentAggregatorService,
        studentRegistrationService: StudentRegistrationService,
    ): StudentFacade {
        return StudentFacade(
            studentAggregatorService,
            studentRegistrationService,
        )
    }

    @Bean
    fun studentAggregatorService(
        studentRepository: StudentRepository,
        studentAccessValidator: StudentAccessValidator,
    ): StudentAggregatorService {
        return StudentAggregatorService(
            studentRepository,
            studentAccessValidator,
        )
    }

    @Bean
    fun studentRegistrationService(
        studentRepository: StudentRepository,
        userFacade: UserFacade,
        parentFacade: ParentFacade,
        courseFacade: CourseFacade,
    ): StudentRegistrationService {
        return StudentRegistrationService(
            studentRepository,
            userFacade,
            parentFacade,
            courseFacade,
        )
    }

    @Bean
    fun studentAccessValidator(): StudentAccessValidator {
        return StudentAccessValidator()
    }
}
