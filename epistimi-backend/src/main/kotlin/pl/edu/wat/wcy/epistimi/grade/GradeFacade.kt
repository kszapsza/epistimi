package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.grade.domain.Grade
import pl.edu.wat.wcy.epistimi.grade.domain.GradeId
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueRequest
import pl.edu.wat.wcy.epistimi.grade.domain.StudentGrades
import pl.edu.wat.wcy.epistimi.grade.domain.SubjectGrades
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeAggregatorService
import pl.edu.wat.wcy.epistimi.grade.domain.service.GradeIssuingService
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import pl.edu.wat.wcy.epistimi.user.domain.User

class GradeFacade(
    private val gradeAggregatorService: GradeAggregatorService,
    private val gradeIssuingService: GradeIssuingService,
) {
    fun getGradeById(
        requester: User,
        gradeId: GradeId,
    ): Grade {
        return gradeAggregatorService.getGradeById(requester, gradeId)
    }

    fun getStudentGrades(
        requester: User,
        subjectIds: List<SubjectId>?,
    ): StudentGrades {
        return gradeAggregatorService.getStudentGrades(requester, subjectIds)
    }

    fun getSubjectGrades(
        requester: User,
        subjectId: SubjectId,
        studentIds: List<StudentId>?,
    ): SubjectGrades {
        return gradeAggregatorService.getSubjectGrades(requester, subjectId, studentIds)
    }

    fun issueGrade(
        requester: User,
        gradeIssueRequest: GradeIssueRequest,
    ): Grade {
        return gradeIssuingService.issueGrade(requester, gradeIssueRequest)
    }
}
