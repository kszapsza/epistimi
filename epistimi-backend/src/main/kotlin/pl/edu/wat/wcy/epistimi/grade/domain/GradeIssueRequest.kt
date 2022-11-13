package pl.edu.wat.wcy.epistimi.grade.domain

import pl.edu.wat.wcy.epistimi.student.domain.StudentId
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectId
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class GradeIssueRequest(
    val subjectId: SubjectId,
    val studentId: StudentId,
    val categoryId: GradeCategoryId,
    val value: GradeValue,
    @field:Min(1) @field:Max(2) val semester: Int,
    @field:Min(1) @field:Max(10) val weight: Int,
    val countTowardsAverage: Boolean,
    val comment: String?,
)