package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.GradeFacade
import pl.edu.wat.wcy.epistimi.grade.domain.access.ClassificationGradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeCategoryService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeIssuingService
import pl.edu.wat.wcy.epistimi.grade.domain.service.StudentGradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.SubjectGradeAggregatorService
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
        studentGradeAggregatorService: StudentGradeAggregatorService,
        subjectGradeAggregatorService: SubjectGradeAggregatorService,
    ): GradeAggregatorService {
        return GradeAggregatorService(
            gradeRepository,
            gradeAccessValidator,
            studentGradeAggregatorService,
            subjectGradeAggregatorService,
        )
    }

    @Bean
    fun studentGradeAggregatorService(
        gradeRepository: GradeRepository,
        gradeAccessValidator: GradeAccessValidator,
        classificationGradeRepository: ClassificationGradeRepository,
        classificationGradeAccessValidator: ClassificationGradeAccessValidator,
        studentFacade: StudentFacade,
        parentFacade: ParentFacade,
    ): StudentGradeAggregatorService {
        return StudentGradeAggregatorService(
            gradeRepository,
            gradeAccessValidator,
            classificationGradeRepository,
            classificationGradeAccessValidator,
            studentFacade,
            parentFacade,
        )
    }

    @Bean
    fun subjectGradeAggregatorService(
        subjectFacade: SubjectFacade,
        gradeRepository: GradeRepository,
        gradeAccessValidator: GradeAccessValidator,
        classificationGradeRepository: ClassificationGradeRepository,
        classificationGradeAccessValidator: ClassificationGradeAccessValidator,
    ): SubjectGradeAggregatorService {
        return SubjectGradeAggregatorService(
            subjectFacade,
            gradeRepository,
            gradeAccessValidator,
            classificationGradeRepository,
            classificationGradeAccessValidator,
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
