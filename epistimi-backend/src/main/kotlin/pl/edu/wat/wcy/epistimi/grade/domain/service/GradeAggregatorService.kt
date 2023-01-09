package pl.edu.wat.wcy.epistimi.grade.domain.service

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.StudentsGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.access.GradeAccessValidator
import pl.edu.wat.wcy.epistimi.grade.domain.port.GradeRepository
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeAggregatorService(
    private val gradeRepository: GradeRepository,
    private val gradeAccessValidator: GradeAccessValidator,
    private val studentGradeAggregatorService: StudentGradeAggregatorService,
    private val subjectGradeAggregatorService: SubjectGradeAggregatorService,
) {
    fun getGradeById(requester: User, gradeId: GradeId): Grade {
        return gradeRepository.findById(gradeId)
            .also { grade ->
                if (gradeAccessValidator.canRetrieve(requester, grade)) {
                    throw GradeNotFoundException(grade.id!!)
                }
            }
    }

    fun getStudentGrades(
        requester: User,
        subjectIds: List<SubjectId>?,
    ): StudentsGrades {
        return studentGradeAggregatorService.getStudentGrades(requester, subjectIds)
    }

    fun getSubjectGrades(
        requester: User,
        subjectId: SubjectId,
        studentIds: List<StudentId>?,
    ): SubjectGrades {
        return subjectGradeAggregatorService.getSubjectGrades(requester, subjectId, studentIds)
    }
}
