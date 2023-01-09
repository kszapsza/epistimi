package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.GradeCategoryFacade
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeCategoryAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeCategoryRepository
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeCategoryService
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade

@Configuration
class GradeCategoryConfiguration {
    @Bean
    fun gradeCategoryFacade(
        gradeCategoryService: GradeCategoryService,
    ): GradeCategoryFacade {
        return GradeCategoryFacade(gradeCategoryService)
    }

    @Bean
    fun gradeCategoryService(
        gradeCategoryRepository: GradeCategoryRepository,
        gradeCategoryAccessValidator: GradeCategoryAccessValidator,
        subjectFacade: SubjectFacade,
    ): GradeCategoryService {
        return GradeCategoryService(
            gradeCategoryRepository,
            gradeCategoryAccessValidator,
            subjectFacade,
        )
    }

    @Bean
    fun gradeCategoryAccessValidator(): GradeCategoryAccessValidator {
        return GradeCategoryAccessValidator()
    }
}
