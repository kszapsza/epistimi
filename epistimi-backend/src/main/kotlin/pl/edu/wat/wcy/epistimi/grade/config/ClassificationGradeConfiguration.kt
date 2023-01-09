package pl.edu.wat.wcy.epistimi.grade.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.grade.ClassificationGradeFacade
import pl.edu.wat.wcy.epistimi.grade.domain.access.ClassificationGradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.ClassificationGradeRepository
import pl.edu.wat.wcy.epistimi.grade.domain.service.ClassificationGradeIssuingService
import pl.edu.wat.wcy.epistimi.student.StudentFacade
import pl.edu.wat.wcy.epistimi.subject.SubjectFacade
import pl.edu.wat.wcy.epistimi.teacher.TeacherFacade

@Configuration
class ClassificationGradeConfiguration {
    @Bean
    fun classificationGradeFacade(
        classificationGradeIssuingService: ClassificationGradeIssuingService,
    ): ClassificationGradeFacade {
        return ClassificationGradeFacade(classificationGradeIssuingService)
    }

    @Bean
    fun classificationGradeIssuingService(
        classificationGradeRepository: ClassificationGradeRepository,
        classificationGradeAccessValidator: ClassificationGradeAccessValidator,
        subjectFacade: SubjectFacade,
        studentFacade: StudentFacade,
        teacherFacade: TeacherFacade,
    ): ClassificationGradeIssuingService {
        return ClassificationGradeIssuingService(
            classificationGradeRepository,
            classificationGradeAccessValidator,
            subjectFacade,
            studentFacade,
            teacherFacade
        )
    }

    @Bean
    fun classificationGradeAccessValidator(): ClassificationGradeAccessValidator {
        return ClassificationGradeAccessValidator()
    }
}
