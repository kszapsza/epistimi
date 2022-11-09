package pl.edu.wat.wcy.epistimi.grade

import pl.edu.wat.wcy.epistimi.student.StudentId
import pl.edu.wat.wcy.epistimi.subject.SubjectId

data class GradeFilters(
    val subjectId: SubjectId,
    val studentIds: List<StudentId>?,
)
