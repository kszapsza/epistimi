package pl.edu.wat.wcy.epistimi.grade.domain.request

import pl.edu.wat.wcy.epistimi.grade.domain.ClassificationGradePeriod
import pl.edu.wat.wcy.epistimi.grade.domain.GradeValue
import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId

data class ClassificationGradeIssueRequest(
    val subjectId: SubjectId,
    val studentId: StudentId,
    val period: ClassificationGradePeriod,
    val value: GradeValue,
    val isProposal: Boolean,
)
