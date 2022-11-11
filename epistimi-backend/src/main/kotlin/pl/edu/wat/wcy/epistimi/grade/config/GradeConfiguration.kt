package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeStatisticsService

@Configuration
class GradeConfiguration {
    @Bean
    fun gradesAggregatorService(
        gradeRepository: GradeRepository,
        gradeStatisticsService: GradeStatisticsService,
        gradeAccessValidator: GradeAccessValidator,
    ): GradeAggregatorService {
        return GradeAggregatorService(
            gradeRepository,
            gradeStatisticsService,
            gradeAccessValidator,
        )
    }

    @Bean
    fun gradesStatisticsService(
        gradeRepository: GradeRepository,
    ): GradeStatisticsService {
        return GradeStatisticsService(gradeRepository)
    }

    @Bean
    fun gradeAccessValidator(): GradeAccessValidator {
        return GradeAccessValidator()
    }
}
