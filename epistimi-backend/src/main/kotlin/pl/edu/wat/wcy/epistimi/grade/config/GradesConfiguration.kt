package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.GradeStatisticsService
import pl.edu.wat.wcy.epistimi.grade.port.GradeRepository

@Configuration
class GradesConfiguration {

    @Bean
    fun gradesAggregatorService(
        gradeRepository: GradeRepository,
        gradeStatisticsService: GradeStatisticsService,
    ): GradeAggregatorService {
        return GradeAggregatorService(gradeRepository, gradeStatisticsService)
    }

    @Bean
    fun gradesStatisticsService(
        gradeRepository: GradeRepository,
    ): GradeStatisticsService {
        return GradeStatisticsService(gradeRepository)
    }
}
