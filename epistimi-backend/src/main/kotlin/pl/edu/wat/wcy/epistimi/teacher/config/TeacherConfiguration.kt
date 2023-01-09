package pl.edu.wat.wcy.epistimi.teacher.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade
import pl.edu.wat.wcy.epistimi.teacher.domain.access.TeacherAccessValidator
import pl.edu.wat.wcy.epistimi.teacher.domain.port.TeacherRepository
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherAggregatorService
import pl.edu.wat.wcy.epistimi.teacher.domain.service.TeacherRegistrationService
import pl.edu.wat.wcy.epistimi.user.UserFacade

@Configuration
class TeacherConfiguration {

    @Bean
    fun teacherFacade(
        teacherAggregatorService: TeacherAggregatorService,
        teacherRegistrationService: TeacherRegistrationService,
    ): TeacherFacade {
        return TeacherFacade(
            teacherAggregatorService,
            teacherRegistrationService,
        )
    }

    @Bean
    fun teacherAggregatorService(
        teacherRepository: TeacherRepository,
        teacherAccessValidator: TeacherAccessValidator,
    ): TeacherAggregatorService {
        return TeacherAggregatorService(
            teacherRepository,
            teacherAccessValidator,
        )
    }

    @Bean
    fun teacherRegistrationService(
        teacherRepository: TeacherRepository,
        userFacade: UserFacade,
    ): TeacherRegistrationService {
        return TeacherRegistrationService(
            teacherRepository,
            userFacade,
        )
    }

    @Bean
    fun teacherAccessValidator(): TeacherAccessValidator {
        return TeacherAccessValidator()
    }
}
