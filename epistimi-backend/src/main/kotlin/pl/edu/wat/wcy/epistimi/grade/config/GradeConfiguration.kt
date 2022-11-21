package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.GradeFacade
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeCategoryService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeIssuingService
import pl.edu.wat.wcy.epistimi.parent.ParentFacade
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade

@Configuration
class GradeConfiguration {
    @Bean
    fun gradeFacade(
        gradeAggregatorService: GradeAggregatorService,
        gradeIssuingService: GradeIssuingService,
    ): GradeFacade {
        return GradeFacade(
            gradeAggregatorService,
            gradeIssuingService,
        )
    }

    @Bean
    fun gradeAggregatorService(
        gradeRepository: GradeRepository,
        gradeAccessValidator: GradeAccessValidator,
        studentFacade: StudentFacade,
        subjectFacade: SubjectFacade,
        parentFacade: ParentFacade,
    ): GradeAggregatorService {
        return GradeAggregatorService(
            gradeRepository,
            gradeAccessValidator,
            studentFacade,
            subjectFacade,
            parentFacade,
        )
    }

    @Bean
    fun gradeAccessValidator(): GradeAccessValidator {
        return GradeAccessValidator()
    }

    @Bean
    fun gradeIssuingService(
        gradeRepository: GradeRepository,
        gradeAccessValidator: GradeAccessValidator,
        gradeCategoryService: GradeCategoryService,
        subjectFacade: SubjectFacade,
        studentFacade: StudentFacade,
        teacherFacade: TeacherFacade,
    ): GradeIssuingService {
        return GradeIssuingService(
            gradeRepository,
            gradeAccessValidator,
            gradeCategoryService,
            subjectFacade,
            studentFacade,
            teacherFacade,
        )
    }
}
